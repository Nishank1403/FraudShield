package com.fraudshield.service;

import com.fraudshield.dto.TransactionRequest;

public interface QueuePublisher {
    void enqueue(TransactionRequest request);
}
