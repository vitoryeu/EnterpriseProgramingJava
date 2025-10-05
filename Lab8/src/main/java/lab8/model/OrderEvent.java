package lab8.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderEvent {
    private String orderId;
    private OrderPriority priority;
    private LocalDateTime createdAt;
    private Map<String, Object> payload;
    private int retryCount;
}
