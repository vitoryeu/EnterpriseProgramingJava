package Processors;

import Models.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedProcessor {
    public Map<String, Map<Integer, Double>> aggregateByCategoriesPar(List<DataRecord> records) {
        return records.parallelStream().collect(Collectors.groupingBy(
                DataRecord::getCategory,
                Collectors.groupingBy(
                        DataRecord::getPriority,
                        Collectors.summingDouble(DataRecord::getValue)
                )));
    }

    public Map<String, List<DataRecord>> findTopNByCategoryPar(List<DataRecord> records, int n) {
        return records.parallelStream().collect(Collectors.groupingBy(
                DataRecord::getCategory,
                Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.stream()
                                .sorted(Comparator.comparingDouble(DataRecord::getValue).reversed())
                                .limit(n).toList()
                )));
    }

    public Map<LocalDate, DoubleSummaryStatistics> analyzeByTimeIntervalsPar(List<DataRecord> records) {
        return records.parallelStream().collect(Collectors.groupingBy(
                r -> r.getTimestamp().toLocalDate(),
                Collectors.summarizingDouble(DataRecord::getValue)
        ));
    }
}