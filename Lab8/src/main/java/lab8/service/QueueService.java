package lab8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import lab8.model.OrderEvent;
import lab8.model.OrderPriority;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueueService {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final LoadBalancer loadBalancer;
    private final QueueMonitor queueMonitor;

    @Value("${queue.order.topics.urgent}") private String urgentTopic;
    @Value("${queue.order.topics.vip}") private String vipTopic;
    @Value("${queue.order.topics.standard}") private String standardTopic;

    public void enqueueOrder(OrderEvent event) {
        String topic = determineTopicForPriority(event.getPriority());
        Integer partition = loadBalancer.determinePartition(event);
        try {
            CompletableFuture<SendResult<String, OrderEvent>> fut =
                kafkaTemplate.send(topic, partition, event.getOrderId(), event);
            fut.whenComplete((res, ex) -> {
                if (ex != null) {
                    log.error("Failed to enqueue order {}", event.getOrderId(), ex);
                } else {
                    log.info("Enqueued order {} to topic={}, partition={}", event.getOrderId(), topic, partition);
                    queueMonitor.recordEnqueue(event);
                }
            });
        } catch (Exception e) {
            log.error("Failed to enqueue order {}", event.getOrderId(), e);
            event.setRetryCount(event.getRetryCount() + 1);
        }
    }

    private String determineTopicForPriority(OrderPriority priority) {
        return switch (priority) {
            case URGENT -> urgentTopic;
            case VIP -> vipTopic;
            case STANDARD -> standardTopic;
        };
    }
}
