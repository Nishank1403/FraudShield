package com.fraudshield.store;

import com.fraudshield.dto.TransactionResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionResultStore {
    private final Map<String, TransactionResult> store = new ConcurrentHashMap<>();

    public void put(TransactionResult result) {
        store.put(result.getTransactionId(), result);
    }

    public TransactionResult get(String transactionId) {
        return store.get(transactionId);
    }
}
