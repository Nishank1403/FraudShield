package com.fraudshield.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraudshield.dto.TransactionRequest;
import com.fraudshield.dto.TransactionResult;
import com.fraudshield.service.FraudDetectionService;
import com.fraudshield.store.TransactionResultStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "fraudshield.queue.mode", havingValue = "kafka")
public class KafkaQueueConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaQueueConsumer.class);

    private final FraudDetectionService detectionService;
    private final TransactionResultStore resultStore;
    private final ObjectMapper objectMapper;

    @Value("${fraudshield.kafka.topic}")
    private String topic;

    public KafkaQueueConsumer(FraudDetectionService detectionService,
                              TransactionResultStore resultStore,
                              ObjectMapper objectMapper) {
        this.detectionService = detectionService;
        this.resultStore = resultStore;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${fraudshield.kafka.topic}", groupId = "fraudshield-consumer")
    public void consume(String payload) throws Exception {
        TransactionRequest request = objectMapper.readValue(payload, TransactionRequest.class);
        TransactionResult result = detectionService.evaluate(request);
        resultStore.put(result);
        log.info("Kafka processed transaction {} flagged={} score={} latencyMs={}",
                result.getTransactionId(), result.isFlagged(), result.getRiskScore(), result.getLatencyMs());
    }
}
