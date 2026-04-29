package com.fraudshield.rules;

import com.fraudshield.dto.TransactionRequest;

public class CountryMismatchRule implements FraudRule {
    @Override
    public RuleResult evaluate(TransactionRequest request) {
        if (request.getCountry() != null && request.getCardCountry() != null
                && !request.getCountry().equalsIgnoreCase(request.getCardCountry())) {
            return new RuleResult("COUNTRY_MISMATCH", 40, "Billing country mismatch");
        }
        return new RuleResult("COUNTRY_MISMATCH", 0, "");
    }
}
