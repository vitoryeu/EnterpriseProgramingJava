package lab8.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lab8.model.OrderEvent;
import lab8.model.OrderPriority;
import lab8.service.MetricsCollector;
import lab8.service.QueueMonitor;
import lab8.service.QueueService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final QueueService queueService;
    private final QueueMonitor queueMonitor;
    private final MetricsCollector metrics;

    @PostMapping("/orders")
    public ResponseEntity<?> enqueue(@RequestBody CreateOrderRequest req) {
        OrderEvent event = OrderEvent.builder()
                .orderId(req.getOrderId() != null ? req.getOrderId() : UUID.randomUUID().toString())
                .priority(req.getPriority() != null ? req.getPriority() : OrderPriority.STANDARD)
                .createdAt(LocalDateTime.now())
                .payload(req.getPayload() != null ? req.getPayload() : Map.of("item","demo","qty",1))
                .retryCount(0)
                .build();
        queueService.enqueueOrder(event);
        return ResponseEntity.accepted().body(Map.of("enqueued", true, "orderId", event.getOrderId(), "priority", event.getPriority()));
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestParam(name = "count", defaultValue = "100") int count) {
        Random rnd = new Random();
        for (int i = 0; i < count; i++) {
            OrderEvent e = OrderEvent.builder()
                    .orderId(UUID.randomUUID().toString())
                    .priority(pick(rnd))
                    .createdAt(LocalDateTime.now())
                    .payload(Map.of("sku", "SKU-" + rnd.nextInt(10_000), "qty", 1 + rnd.nextInt(5)))
                    .retryCount(0)
                    .build();
            queueService.enqueueOrder(e);
        }
        return ResponseEntity.ok(Map.of("generated", count));
    }

    private OrderPriority pick(Random rnd) {
        int x = rnd.nextInt(100);
        if (x < 10) return OrderPriority.URGENT;
        if (x < 40) return OrderPriority.VIP;
        return OrderPriority.STANDARD;
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> metrics() { return ResponseEntity.ok(metrics.snapshot()); }

    @GetMapping("/queues")
    public ResponseEntity<?> queues() { return ResponseEntity.ok(queueMonitor.getQueueSizes()); }

    @Data
    public static class CreateOrderRequest {
        private String orderId;
        private OrderPriority priority;
        private Map<String, Object> payload;
    }
}
