package com.fraudshield.store;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionHistoryStore {
    private final Map<String, List<Instant>> history = new ConcurrentHashMap<>();

    public void record(String userId, Instant timestamp) {
        history.compute(userId, (key, list) -> {
            List<Instant> updated = list == null ? new ArrayList<>() : new ArrayList<>(list);
            updated.add(timestamp);
            return updated;
        });
    }

    public int countRecent(String userId, int seconds) {
        List<Instant> list = history.getOrDefault(userId, List.of());
        Instant cutoff = Instant.now().minusSeconds(seconds);
        return (int) list.stream().filter(t -> t.isAfter(cutoff)).count();
    }
}
