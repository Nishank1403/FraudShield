package com.fraudshield.rules;

public class RuleResult {
    private final String ruleName;
    private final int score;
    private final String reason;

    public RuleResult(String ruleName, int score, String reason) {
        this.ruleName = ruleName;
        this.score = score;
        this.reason = reason;
    }

    public String getRuleName() {
        return ruleName;
    }

    public int getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }
}
