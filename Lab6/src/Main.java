import Models.*;
import Processors.*;
import Utils.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        long N = args.length > 0 ? Long.parseLong(args[0]) : 1_000_000L;

        System.out.println("Generating " + N + " records...");
        List<DataRecord> records = DataGenerator.generate(N);

        DataProcessor dp = new DataProcessor();
        AdvancedProcessor ap = new AdvancedProcessor();
        AsyncProcessor async = new AsyncProcessor();
        PerformanceAnalyzer perf = new PerformanceAnalyzer();

        perf.measureMemoryUsage(records);
        perf.comparePerformance(records, dp);
        perf.analyzeSizeImpact(records, dp);

        System.out.println("\n=== Advanced PAR aggregations ===");
        var agg = ap.aggregateByCategoriesPar(records);
        System.out.println("Categories: " + agg.keySet());

        var top = ap.findTopNByCategoryPar(records, 3);
        System.out.println("Top3 in 'A': " + top.getOrDefault("A", List.of()));

        var byDay = ap.analyzeByTimeIntervalsPar(records);
        System.out.println("Days counted: " + byDay.size());

        System.out.println("\n=== Async processing ===");
        CompletableFuture<Map<String, Double>> avgF = async.processAsync(records);
        System.out.println("Avg by category (async): " + avgF.join());

        List<List<DataRecord>> batches = List.of(
                records.subList(0, Math.min(200_000, records.size())),
                records.subList(Math.min(200_000, records.size()), Math.min(400_000, records.size()))
        );
        var futures = async.processBatch(batches);
        var combined = async.combineResults(new ArrayList<>(futures));
        System.out.println("Combined results keys: " + combined.join().keySet());

        System.out.println("\nDone.");
    }
}