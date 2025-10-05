package lab8.service;

import org.springframework.stereotype.Service;
import lab8.model.OrderEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricsCollector {
    // Produced/Consumed counters
    private final LongAdder produced = new LongAdder();
    private final LongAdder consumed = new LongAdder();
    private final Map<String, LongAdder> byPriorityProduced = new ConcurrentHashMap<>();
    private final Map<String, LongAdder> byPriorityConsumed = new ConcurrentHashMap<>();

    // Processing latency (work time)
    private final AtomicLong totalLatencyMs = new AtomicLong(0);
    private final AtomicLong minLatencyMs = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxLatencyMs = new AtomicLong(0);
    private final Map<String, AtomicLong> latencySumByPriority = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> latencyCountByPriority = new ConcurrentHashMap<>();
    private static final int RING_SIZE = 1024;
    private final Map<String, long[]> ringByPriority = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> ringIdxByPriority = new ConcurrentHashMap<>();

    // Queue wait metrics (enqueue -> start processing)
    private final AtomicLong totalWaitMs = new AtomicLong(0);
    private final AtomicLong minWaitMs = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxWaitMs = new AtomicLong(0);
    private final Map<String, AtomicLong> waitSumByPriority = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> waitCountByPriority = new ConcurrentHashMap<>();
    private final Map<String, long[]> waitRingByPriority = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> waitIdxByPriority = new ConcurrentHashMap<>();

    // SLA breaches per priority
    private final Map<String, LongAdder> slaBreachesByPriority = new ConcurrentHashMap<>();

    public void recordEnqueued(OrderEvent e) {
        produced.increment();
        byPriorityProduced.computeIfAbsent(e.getPriority().name(), k -> new LongAdder()).increment();
    }

    public void recordDequeued(OrderEvent e, long processingLatencyMs, long waitMs) {
        consumed.increment();
        String p = e.getPriority().name();
        byPriorityConsumed.computeIfAbsent(p, k -> new LongAdder()).increment();

        // processing latency
        totalLatencyMs.addAndGet(processingLatencyMs);
        minLatencyMs.getAndUpdate(prev -> Math.min(prev, processingLatencyMs));
        maxLatencyMs.getAndUpdate(prev -> Math.max(prev, processingLatencyMs));
        latencySumByPriority.computeIfAbsent(p, k -> new AtomicLong()).addAndGet(processingLatencyMs);
        latencyCountByPriority.computeIfAbsent(p, k -> new AtomicLong()).incrementAndGet();
        long[] ring = ringByPriority.computeIfAbsent(p, k -> new long[RING_SIZE]);
        AtomicInteger idx = ringIdxByPriority.computeIfAbsent(p, k -> new AtomicInteger(0));
        ring[idx.getAndIncrement() & (RING_SIZE - 1)] = processingLatencyMs;

        // wait latency
        totalWaitMs.addAndGet(waitMs);
        minWaitMs.getAndUpdate(prev -> Math.min(prev, waitMs));
        maxWaitMs.getAndUpdate(prev -> Math.max(prev, waitMs));
        waitSumByPriority.computeIfAbsent(p, k -> new AtomicLong()).addAndGet(waitMs);
        waitCountByPriority.computeIfAbsent(p, k -> new AtomicLong()).incrementAndGet();
        long[] wring = waitRingByPriority.computeIfAbsent(p, k -> new long[RING_SIZE]);
        AtomicInteger widx = waitIdxByPriority.computeIfAbsent(p, k -> new AtomicInteger(0));
        wring[widx.getAndIncrement() & (RING_SIZE - 1)] = waitMs;

        // SLA thresholds
        long threshold = switch (e.getPriority()) {
            case URGENT -> 5_000L;      // 5 sec
            case VIP -> 30_000L;        // 30 sec
            case STANDARD -> 120_000L;  // 2 min
        };
        if (waitMs > threshold) {
            slaBreachesByPriority.computeIfAbsent(p, k -> new LongAdder()).increment();
        }
    }

    public java.util.Map<String, Object> snapshot() {
        java.util.Map<String, Object> map = new java.util.concurrent.ConcurrentHashMap<>();
        long prod = produced.sum();
        long cons = consumed.sum();
        map.put("producedTotal", prod);
        map.put("consumedTotal", cons);
        map.put("producedByPriority", toSimple(byPriorityProduced));
        map.put("consumedByPriority", toSimple(byPriorityConsumed));

        // processing latency totals
        long avgProc = cons > 0 ? totalLatencyMs.get() / cons : 0;
        map.put("processingLatencyMs", java.util.Map.of(
                "min", minLatencyMs.get() == Long.MAX_VALUE ? 0 : minLatencyMs.get(),
                "avg", avgProc,
                "max", maxLatencyMs.get()
        ));

        java.util.Map<String, Object> perPriorityProc = new java.util.concurrent.ConcurrentHashMap<>();
        for (String p : latencySumByPriority.keySet()) {
            long sum = latencySumByPriority.get(p).get();
            long cnt = latencyCountByPriority.getOrDefault(p, new AtomicLong()).get();
            long avgP = cnt > 0 ? sum / cnt : 0;
            long p95 = 0;
            long[] ring = ringByPriority.get(p);
            if (ring != null) {
                long[] copy = ring.clone();
                java.util.Arrays.sort(copy);
                p95 = copy[(int)Math.floor(0.95 * (copy.length - 1))];
            }
            perPriorityProc.put(p, java.util.Map.of("avgMs", avgP, "p95Ms", p95, "samples", cnt));
        }
        map.put("processingLatencyByPriority", perPriorityProc);

        // wait latency totals
        long avgWait = cons > 0 ? totalWaitMs.get() / cons : 0;
        map.put("waitMs", java.util.Map.of(
                "min", minWaitMs.get() == Long.MAX_VALUE ? 0 : minWaitMs.get(),
                "avg", avgWait,
                "max", maxWaitMs.get()
        ));

        java.util.Map<String, Object> perPriorityWait = new java.util.concurrent.ConcurrentHashMap<>();
        for (String p : waitSumByPriority.keySet()) {
            long sum = waitSumByPriority.get(p).get();
            long cnt = waitCountByPriority.getOrDefault(p, new AtomicLong()).get();
            long avgP = cnt > 0 ? sum / cnt : 0;
            long p95 = 0;
            long[] ring = waitRingByPriority.get(p);
            if (ring != null) {
                long[] copy = ring.clone();
                java.util.Arrays.sort(copy);
                p95 = copy[(int)Math.floor(0.95 * (copy.length - 1))];
            }
            long breaches = slaBreachesByPriority.getOrDefault(p, new LongAdder()).sum();
            perPriorityWait.put(p, java.util.Map.of(
                    "avgMs", avgP, "p95Ms", p95, "samples", cnt, "slaBreaches", breaches
            ));
        }
        map.put("waitByPriority", perPriorityWait);
        return map;
    }

    private static java.util.Map<String, Long> toSimple(Map<String, LongAdder> m) {
        java.util.Map<String, Long> out = new java.util.concurrent.ConcurrentHashMap<>();
        m.forEach((k, v) -> out.put(k, v.sum()));
        return out;
    }
}
