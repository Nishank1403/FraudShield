package com.fraudshield.controller;

import com.fraudshield.dto.TransactionRequest;
import com.fraudshield.service.QueuePublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/simulate")
public class SimulationController {
    private final QueuePublisher queuePublisher;
    private final Random random = new Random();

    public SimulationController(QueuePublisher queuePublisher) {
        this.queuePublisher = queuePublisher;
    }

    @PostMapping
    public ResponseEntity<String> simulate(@RequestParam(defaultValue = "10") int count) {
        for (int i = 0; i < count; i++) {
            queuePublisher.enqueue(randomTransaction());
        }
        return ResponseEntity.accepted().body("Queued " + count + " transactions");
    }

    private TransactionRequest randomTransaction() {
        TransactionRequest request = new TransactionRequest();
        request.setTransactionId("sim-" + UUID.randomUUID());
        request.setUserId("u-" + (1000 + random.nextInt(200)));
        request.setAmount(10 + random.nextDouble() * 20000);
        request.setCurrency("USD");
        request.setMerchantId(random.nextInt(20) == 0 ? "m-666" : "m-" + random.nextInt(100));
        request.setCountry(random.nextBoolean() ? "US" : "IN");
        request.setCardCountry(random.nextInt(5) == 0 ? "FR" : request.getCountry());
        request.setTimestamp(Instant.now());
        return request;
    }
}
