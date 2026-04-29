package com.fraudshield.store;

import com.fraudshield.dto.TransactionResult;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TransactionResultStore {
    private static final int MAX_RECENT = 200;

    private final Map<String, TransactionResult> store = new ConcurrentHashMap<>();
    private final Deque<String> order = new ConcurrentLinkedDeque<>();

    public void put(TransactionResult result) {
        store.put(result.getTransactionId(), result);
        order.addFirst(result.getTransactionId());
        while (order.size() > MAX_RECENT) {
            String removed = order.pollLast();
            if (removed != null && !order.contains(removed)) {
                store.remove(removed);
            }
        }
    }

    public TransactionResult get(String transactionId) {
        return store.get(transactionId);
    }

    public List<TransactionResult> listRecent(int limit) {
        List<TransactionResult> results = new ArrayList<>();
        int count = 0;
        for (String id : order) {
            TransactionResult result = store.get(id);
            if (result != null) {
                results.add(result);
                count++;
            }
            if (count >= limit) {
                break;
            }
        }
        return results;
    }
}
