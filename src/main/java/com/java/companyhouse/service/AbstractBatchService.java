package com.java.companyhouse.service;

import com.java.companyhouse.model.receiver.CompanySnapshot;
import com.java.companyhouse.model.receiver.TopicBatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public abstract class AbstractBatchService<T> {

    private final TransactionTemplate transactionTemplate;

    public void receive(TopicBatchRequest<T> request) {
        if (request == null || request.getCompanies() == null) {
            return;
        }
        for (CompanySnapshot<T> snapshot : request.getCompanies()) {
            transactionTemplate.executeWithoutResult(status -> processSnapshot(snapshot));
        }
    }

    protected abstract void processSnapshot(CompanySnapshot<T> snapshot);
}