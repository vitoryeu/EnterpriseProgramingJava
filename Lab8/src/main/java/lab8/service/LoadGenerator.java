package lab8.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lab8.model.OrderEvent;
import lab8.model.OrderPriority;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoadGenerator {
    private final QueueService queueService;
    private final Random rnd = new Random();

    @Value("${queue.generator.enabled:false}") private boolean enabled;
    @Value("${queue.generator.rate-per-sec:20}") private int ratePerSec;

    private ScheduledExecutorService ses;

    @PostConstruct
    public void startIfEnabled() {
        if (!enabled) return;
        ses = Executors.newSingleThreadScheduledExecutor();
        long periodNanos = 1_000_000_000L / Math.max(1, ratePerSec);
        ses.scheduleAtFixedRate(this::emitOne, periodNanos, periodNanos, TimeUnit.NANOSECONDS);
        log.info("LoadGenerator started: {} msg/sec", ratePerSec);
    }

    @PreDestroy
    public void stop() { if (ses != null) ses.shutdownNow(); }

    private void emitOne() {
        try {
            OrderPriority pr = pickPriority();
            OrderEvent e = OrderEvent.builder()
                    .orderId(UUID.randomUUID().toString())
                    .priority(pr)
                    .createdAt(LocalDateTime.now())
                    .payload(Map.of("sku", "SKU-" + rnd.nextInt(10_000), "qty", 1 + rnd.nextInt(5)))
                    .retryCount(0)
                    .build();
            queueService.enqueueOrder(e);
        } catch (Exception ex) {
            log.warn("LoadGenerator emit failed", ex);
        }
    }

    private OrderPriority pickPriority() {
        // Weighted mix: URGENT 10%, VIP 30%, STANDARD 60%
        int x = rnd.nextInt(100);
        if (x < 10) return OrderPriority.URGENT;
        if (x < 40) return OrderPriority.VIP;
        return OrderPriority.STANDARD;
    }
}
