package com.java.companyhouse.service;

import com.java.companyhouse.model.receiver.CompanySnapshot;
import com.java.companyhouse.model.receiver.TopicBatchRequest;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractBatchService<T> {

    @Transactional
    public void receive(TopicBatchRequest<T> request) {
        for (CompanySnapshot<T> snapshot : request.getCompanies()) {
            processSnapshot(snapshot);
        }
    }

    protected abstract void processSnapshot(CompanySnapshot<T> snapshot);
}