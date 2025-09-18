package Utils;

import Models.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public final class DataGenerator {
    private static final List<String> CATEGORIES = List.of("A","B","C","D","E");
    private static final List<String> STATUSES   = List.of("NEW","OK","WARN","ERR");
    private DataGenerator() {}

    public static List<DataRecord> generate(long count) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        LocalDateTime base = LocalDateTime.now().minusDays(60);

        return LongStream.range(0, count).parallel().mapToObj(i -> {
            String cat = CATEGORIES.get(rnd.nextInt(CATEGORIES.size()));
            String status = STATUSES.get(rnd.nextInt(STATUSES.size()));
            double value = rnd.nextDouble(0, 1000);
            int priority = rnd.nextInt(1, 6);
            LocalDateTime ts = base.plusMinutes(rnd.nextLong(0, 60L * 24 * 60));
            List<String> tags = rnd.nextBoolean()
                    ? List.of("hot","etl") : List.of("cold","batch");
            return new DataRecord(i, cat, value, ts, priority, status, tags);
        }).collect(Collectors.toList());
    }
}