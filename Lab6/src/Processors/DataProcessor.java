package Processors;

import Models.*;

import java.util.*;
import java.util.stream.Collectors;

public class DataProcessor {
    public List<DataRecord> filterRecordsSeq(List<DataRecord> records, String category, double minValue) {
        return records.stream()
                .filter(r -> r.getCategory().equals(category) && r.getValue() >= minValue)
                .toList();
    }

    public Map<String, DoubleSummaryStatistics> calculateStatisticsSeq(List<DataRecord> records) {
        return records.stream().collect(Collectors.groupingBy(
                DataRecord::getCategory,
                Collectors.summarizingDouble(DataRecord::getValue)
        ));
    }

    public Map<Integer, List<DataRecord>> groupByPrioritySeq(List<DataRecord> records) {
        return records.stream().collect(Collectors.groupingBy(DataRecord::getPriority));
    }

    public List<DataRecord> filterRecordsPar(List<DataRecord> records, String category, double minValue) {
        return records.parallelStream()
                .filter(r -> r.getCategory().equals(category) && r.getValue() >= minValue)
                .toList();
    }

    public Map<String, DoubleSummaryStatistics> calculateStatisticsPar(List<DataRecord> records) {
        return records.parallelStream().collect(Collectors.groupingBy(
                DataRecord::getCategory,
                Collectors.summarizingDouble(DataRecord::getValue)
        ));
    }

    public Map<Integer, List<DataRecord>> groupByPriorityPar(List<DataRecord> records) {
        return records.parallelStream().collect(Collectors.groupingBy(DataRecord::getPriority));
    }
}
