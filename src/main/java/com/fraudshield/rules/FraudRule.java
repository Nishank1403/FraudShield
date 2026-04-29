package com.fraudshield.rules;

import com.fraudshield.dto.TransactionRequest;

public interface FraudRule {
    RuleResult evaluate(TransactionRequest request);
}
