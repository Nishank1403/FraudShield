package com.fraudshield.service;

import com.fraudshield.dto.TransactionRequest;
import com.fraudshield.dto.TransactionResult;
import com.fraudshield.rules.*;
import com.fraudshield.store.TransactionHistoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class FraudDetectionService {
    private final List<FraudRule> rules;
    private final TransactionHistoryStore historyStore;

    @Value("${fraudshield.risk.threshold}")
    private int threshold;

    public FraudDetectionService(TransactionHistoryStore historyStore) {
        this.historyStore = historyStore;
        this.rules = List.of(
                new AmountRule(),
                new CountryMismatchRule(),
                new MerchantRiskRule(),
                new VelocityRule(historyStore)
        );
    }

    public TransactionResult evaluate(TransactionRequest request) {
        long start = System.currentTimeMillis();
        historyStore.record(request.getUserId(), request.getTimestamp() == null ? Instant.now() : request.getTimestamp());

        int score = 0;
        List<String> reasons = new ArrayList<>();
        for (FraudRule rule : rules) {
            RuleResult result = rule.evaluate(request);
            score += result.getScore();
            if (result.getScore() > 0 && !result.getReason().isBlank()) {
                reasons.add(result.getReason());
            }
        }

        TransactionResult result = new TransactionResult();
        result.setTransactionId(request.getTransactionId());
        result.setRiskScore(score);
        result.setFlagged(score >= threshold);
        result.setReasons(reasons);
        result.setEvaluatedAt(Instant.now());
        result.setLatencyMs(System.currentTimeMillis() - start);
        return result;
    }
}
