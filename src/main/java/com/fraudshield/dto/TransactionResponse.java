package com.fraudshield.dto;

public class TransactionResponse {
    private String ingestionId;
    private String status;

    public TransactionResponse() {}

    public TransactionResponse(String ingestionId, String status) {
        this.ingestionId = ingestionId;
        this.status = status;
    }

    public String getIngestionId() {
        return ingestionId;
    }

    public void setIngestionId(String ingestionId) {
        this.ingestionId = ingestionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
