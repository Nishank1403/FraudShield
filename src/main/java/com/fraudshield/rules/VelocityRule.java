package com.fraudshield.rules;

import com.fraudshield.dto.TransactionRequest;
import com.fraudshield.store.TransactionHistoryStore;

public class VelocityRule implements FraudRule {
    private final TransactionHistoryStore store;

    public VelocityRule(TransactionHistoryStore store) {
        this.store = store;
    }

    @Override
    public RuleResult evaluate(TransactionRequest request) {
        int count = store.countRecent(request.getUserId(), 300);
        if (count >= 5) {
            return new RuleResult("VELOCITY_RULE", 50, "High transaction velocity");
        }
        return new RuleResult("VELOCITY_RULE", 0, "");
    }
}
