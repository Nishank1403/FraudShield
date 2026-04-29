package com.fraudshield.rules;

import com.fraudshield.dto.TransactionRequest;
import java.util.Set;

public class MerchantRiskRule implements FraudRule {
    private final Set<String> riskyMerchants = Set.of("m-666", "m-999", "m-13");

    @Override
    public RuleResult evaluate(TransactionRequest request) {
        if (riskyMerchants.contains(request.getMerchantId())) {
            return new RuleResult("MERCHANT_RISK", 30, "Merchant on risk list");
        }
        return new RuleResult("MERCHANT_RISK", 0, "");
    }
}
