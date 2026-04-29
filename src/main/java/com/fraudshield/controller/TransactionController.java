package com.fraudshield.controller;

import com.fraudshield.dto.TransactionRequest;
import com.fraudshield.dto.TransactionResponse;
import com.fraudshield.dto.TransactionResult;
import com.fraudshield.service.QueuePublisher;
import com.fraudshield.store.TransactionResultStore;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final QueuePublisher queuePublisher;
    private final TransactionResultStore resultStore;

    public TransactionController(QueuePublisher queuePublisher, TransactionResultStore resultStore) {
        this.queuePublisher = queuePublisher;
        this.resultStore = resultStore;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> ingest(@Valid @RequestBody TransactionRequest request) {
        queuePublisher.enqueue(request);
        return ResponseEntity.accepted()
                .body(new TransactionResponse(request.getTransactionId(), "QUEUED"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResult> getResult(@PathVariable("id") String id) {
        TransactionResult result = resultStore.get(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
