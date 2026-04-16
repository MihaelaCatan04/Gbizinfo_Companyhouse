package com.java.companyhouse.service;

import com.java.companyhouse.model.receiver.CompanySnapshot;
import com.java.companyhouse.model.receiver.TopicBatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractBatchService<T> {

    private final TransactionTemplate transactionTemplate;

    public void receive(TopicBatchRequest<T> request) {
        if (request == null || request.getCompanies() == null) return;
        for (CompanySnapshot<T> snapshot : request.getCompanies()) {
            transactionTemplate.executeWithoutResult(status -> processSnapshot(snapshot));
        }
    }

    private void processSnapshot(CompanySnapshot<T> snapshot) {
        if (snapshot == null || snapshot.getCorporateNumber() == null) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<T> entities = snapshot.getEntities() == null ? Collections.emptyList() : snapshot.getEntities();

        for (T entity : entities) {
            String entityCorporateNumber = getCorporateNumber(entity);
            if (entityCorporateNumber != null && !entityCorporateNumber.equals(corporateNumber)) {
                throw new IllegalArgumentException("Entity corporateNumber " + entityCorporateNumber + " does not match snapshot corporateNumber " + corporateNumber);
            }
        }

        acquireLock(corporateNumber);

        if (entities.isEmpty()) {
            onEmpty(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();
        for (T entity : entities) {
            if (entity != null) upsert(entity, syncId);
        }
        cleanup(corporateNumber, syncId);
    }

    protected abstract void acquireLock(String corporateNumber);

    protected void onEmpty(String corporateNumber) {
    }

    protected abstract void upsert(T entity, String syncId);

    protected void cleanup(String corporateNumber, String syncId) {
    }

    protected abstract String getCorporateNumber(T entity);
}