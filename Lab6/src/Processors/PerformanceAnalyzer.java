package Processors;

import Models.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PerformanceAnalyzer {
    public void comparePerformance(List<DataRecord> records, DataProcessor p) {
        System.out.println("=== Compare SEQ vs PAR: filterRecords(category='A', min=500) ===");
        timeIt("SEQ", () -> p.filterRecordsSeq(records, "A", 500));
        timeIt("PAR", () -> p.filterRecordsPar(records, "A", 500));

        System.out.println("=== Statistics by category ===");
        timeIt("SEQ", () -> p.calculateStatisticsSeq(records));
        timeIt("PAR", () -> p.calculateStatisticsPar(records));

        System.out.println("=== Group by priority ===");
        timeIt("SEQ", () -> p.groupByPrioritySeq(records));
        timeIt("PAR", () -> p.groupByPriorityPar(records));
    }

    public void analyzeSizeImpact(List<DataRecord> records, DataProcessor p) {
        System.out.println("\n=== Size impact (PAR filter) ===");
        int[] sizes = {100_000, 300_000, 600_000, records.size()};
        for (int n : sizes) {
            List<DataRecord> sub = records.subList(0, Math.min(n, records.size()));
            timeIt("PAR n=" + sub.size(), () -> p.filterRecordsPar(sub, "B", 250));
        }
    }

    public void measureMemoryUsage(List<DataRecord> records) {
        System.gc();
        sleep(50);
        long used = usedHeap();
        System.out.printf("Heap used (approx): %,.2f MB for %,d records%n",
                used / (1024.0 * 1024.0), records.size());
    }

    private static long usedHeap() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }
    private static void timeIt(String label, Runnable r) {
        long t0 = System.nanoTime();
        Object res = null;
        try {
            r.run();
        } finally {
            long t1 = System.nanoTime();
            System.out.printf("%-10s -> %6d ms%n", label, TimeUnit.NANOSECONDS.toMillis(t1 - t0));
        }
    }
    private static void sleep(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }
}