package com.fraudshield.rules;

import com.fraudshield.dto.TransactionRequest;

public class AmountRule implements FraudRule {
    @Override
    public RuleResult evaluate(TransactionRequest request) {
        if (request.getAmount() != null && request.getAmount() > 10000) {
            return new RuleResult("AMOUNT_RULE", 60, "High transaction amount");
        }
        return new RuleResult("AMOUNT_RULE", 0, "");
    }
}
