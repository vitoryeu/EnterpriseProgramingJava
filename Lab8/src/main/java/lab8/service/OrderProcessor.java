package lab8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import lab8.model.OrderEvent;
import lab8.model.OrderPriority;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProcessor {
    private final QueueMonitor queueMonitor;
    private final WorkSimulator workSimulator;

    @KafkaListener(id="urgent", topics = "${queue.order.topics.urgent}",
            groupId = "order-processing-group-urgent",
            containerFactory = "urgentKafkaListenerContainerFactory")
    public void processUrgent(@Payload OrderEvent event, ConsumerRecord<String, OrderEvent> record) {
        processOrderWithPriority(event, OrderPriority.URGENT, record.partition(), record.offset());
    }

    @KafkaListener(id="vip", topics = "${queue.order.topics.vip}",
            groupId = "order-processing-group-vip",
            containerFactory = "vipKafkaListenerContainerFactory")
    public void processVip(@Payload OrderEvent event, ConsumerRecord<String, OrderEvent> record) {
        processOrderWithPriority(event, OrderPriority.VIP, record.partition(), record.offset());
    }

    @KafkaListener(id="standard", topics = "${queue.order.topics.standard}",
            groupId = "order-processing-group-standard",
            containerFactory = "standardKafkaListenerContainerFactory")
    public void processStandard(@Payload OrderEvent event, ConsumerRecord<String, OrderEvent> record) {
        processOrderWithPriority(event, OrderPriority.STANDARD, record.partition(), record.offset());
    }

    private void processOrderWithPriority(OrderEvent event, OrderPriority priority, int partition, long offset) {
        long startProcess = System.currentTimeMillis();
        long waitMs = 0;
        try {
            if (event.getCreatedAt() != null) {
                waitMs = Duration.between(event.getCreatedAt(), LocalDateTime.now()).toMillis();
                if (waitMs < 0) waitMs = 0;
            }
            workSimulator.doWork(event);
        } catch (Exception e) {
            log.error("Error processing order {}", event.getOrderId(), e);
        } finally {
            long processingMs = System.currentTimeMillis() - startProcess;
            queueMonitor.recordDequeue(event, processingMs, waitMs);
        }
    }
}
