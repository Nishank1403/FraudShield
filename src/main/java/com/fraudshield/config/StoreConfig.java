package com.fraudshield.config;

import com.fraudshield.store.TransactionHistoryStore;
import com.fraudshield.store.TransactionResultStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfig {
    @Bean
    public TransactionHistoryStore transactionHistoryStore() {
        return new TransactionHistoryStore();
    }

    @Bean
    public TransactionResultStore transactionResultStore() {
        return new TransactionResultStore();
    }
}
