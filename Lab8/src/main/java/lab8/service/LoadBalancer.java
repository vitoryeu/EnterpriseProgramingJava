package lab8.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lab8.model.OrderEvent;
import lab8.model.OrderPriority;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancer {
    private final AtomicInteger counter = new AtomicInteger();
    private final int partitionCount;

    public LoadBalancer(@Value("${queue.processing.threads}") int partitionCount) {
        this.partitionCount = Math.max(1, partitionCount);
    }

    public Integer determinePartition(OrderEvent event) {
        if (event.getPriority() == OrderPriority.URGENT) return 0; // fast lane
        return (counter.incrementAndGet() % Math.max(1, partitionCount));
    }
}
