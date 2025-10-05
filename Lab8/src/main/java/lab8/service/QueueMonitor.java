package lab8.service;

import org.springframework.stereotype.Service;
import lab8.model.OrderEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QueueMonitor {
    private final MetricsCollector metrics;
    private final Map<String, AtomicInteger> queueSizes = new ConcurrentHashMap<>();

    public QueueMonitor(MetricsCollector metrics) {
        this.metrics = metrics;
    }

    public void recordEnqueue(OrderEvent event) {
        String topic = event.getPriority().name();
        queueSizes.computeIfAbsent(topic, k -> new AtomicInteger()).incrementAndGet();
        metrics.recordEnqueued(event);
    }

    public void recordDequeue(OrderEvent event, long processingLatencyMs, long waitMs) {
        String topic = event.getPriority().name();
        queueSizes.computeIfAbsent(topic, k -> new AtomicInteger()).decrementAndGet();
        metrics.recordDequeued(event, processingLatencyMs, waitMs);
    }

    public Map<String, Integer> getQueueSizes() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        queueSizes.forEach((k,v) -> map.put(k, v.get()));
        return map;
    }
}
