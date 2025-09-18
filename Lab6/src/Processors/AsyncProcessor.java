package Processors;

import Models.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncProcessor {
    public CompletableFuture<Map<String, Double>> processAsync(List<DataRecord> records) {
        return CompletableFuture.supplyAsync(() ->
                records.parallelStream().collect(Collectors.groupingBy(
                        DataRecord::getCategory,
                        Collectors.averagingDouble(DataRecord::getValue)
                ))
        );
    }

    public List<CompletableFuture<List<DataRecord>>> processBatch(List<List<DataRecord>> batches) {
        return batches.stream().map(batch ->
                CompletableFuture.supplyAsync(() ->
                        batch.parallelStream().filter(r -> "OK".equals(r.getStatus())).toList()
                )
        ).toList();
    }

    public CompletableFuture<Map<String, Object>> combineResults(List<CompletableFuture<?>> futures) {
        CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return all.thenApply(v -> {
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < futures.size(); i++) {
                result.put("res" + i, futures.get(i).join());
            }
            return result;
        });
    }
}