package com.fraudshield.worker;

import com.fraudshield.dto.TransactionRequest;
import com.fraudshield.dto.TransactionResult;
import com.fraudshield.service.FraudDetectionService;
import com.fraudshield.service.TransactionQueueService;
import com.fraudshield.store.TransactionResultStore;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class QueueWorker {
    private static final Logger log = LoggerFactory.getLogger(QueueWorker.class);

    private final TransactionQueueService queueService;
    private final FraudDetectionService detectionService;
    private final TransactionResultStore resultStore;

    @Value("${fraudshield.worker.poll-delay-ms}")
    private long pollDelayMs;

    public QueueWorker(TransactionQueueService queueService,
                       FraudDetectionService detectionService,
                       TransactionResultStore resultStore) {
        this.queueService = queueService;
        this.detectionService = detectionService;
        this.resultStore = resultStore;
    }

    @Scheduled(fixedDelayString = "${fraudshield.worker.poll-delay-ms}")
    public void pollQueue() {
        TransactionRequest request = queueService.dequeue();
        if (request == null) {
            return;
        }
        TransactionResult result = detectionService.evaluate(request);
        resultStore.put(result);
        log.info("Processed transaction {} flagged={} score={} latencyMs={}",
                result.getTransactionId(), result.isFlagged(), result.getRiskScore(), result.getLatencyMs());
    }
}
