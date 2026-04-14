package com.java.companyhouse.service;

import com.java.companyhouse.model.receiver.NestedBatchRequest;
import com.java.companyhouse.model.receiver.NestedSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

@RequiredArgsConstructor
public abstract class AbstractNestedBatchService<S extends NestedSnapshot<T>, T> {

    private final TransactionTemplate transactionTemplate;

    public void receive(NestedBatchRequest<S> request) {
        List<S> snapshots = request == null || request.getSnapshots() == null ? Collections.emptyList() : request.getSnapshots();

        for (S snapshot : snapshots) {
            transactionTemplate.executeWithoutResult(status -> processSnapshot(snapshot));
        }
    }

    private void processSnapshot(S snapshot) {
        if (snapshot == null || snapshot.getCorporateNumber() == null || snapshot.getParentMergeKey() == null) return;

        String corporateNumber = snapshot.getCorporateNumber();
        String parentMergeKey = snapshot.getParentMergeKey();
        List<T> raw = snapshot.getItems() == null ? Collections.emptyList() : snapshot.getItems();

        for (T item : raw) {
            String entityCorporateNumber = getCorporateNumber(item);
            String entityParentMergeKey = getParentMergeKey(item);
            if (entityCorporateNumber != null && !entityCorporateNumber.equals(corporateNumber)) {
                throw new IllegalArgumentException("Entity corporateNumber " + entityCorporateNumber + " does not match snapshot corporateNumber " + corporateNumber);
            }
            if (entityParentMergeKey != null && !entityParentMergeKey.equals(parentMergeKey)) {
                throw new IllegalArgumentException("Entity parentMergeKey " + entityParentMergeKey + " does not match snapshot parentMergeKey " + parentMergeKey);
            }
        }

        acquireLock(corporateNumber);

        if (raw.isEmpty()) {
            deleteAll(corporateNumber, parentMergeKey);
            return;
        }

        String syncId = UUID.randomUUID().toString();
        List<T> items = prepare(raw);

        upsertAll(items, parentMergeKey, corporateNumber, syncId);
        cleanup(corporateNumber, parentMergeKey, syncId);
    }

    protected List<T> prepare(List<T> raw) {
        List<T> items = new ArrayList<>(raw);
        items.removeIf(dto -> dto == null || getMergeKey(dto) == null);
        items.sort(Comparator.comparing(this::getMergeKey));
        return items;
    }

    protected abstract String getMergeKey(T item);

    protected abstract void acquireLock(String corporateNumber);

    protected abstract void deleteAll(String corporateNumber, String parentMergeKey);

    protected abstract void upsertAll(List<T> items, String parentMergeKey, String corporateNumber, String syncId);

    protected abstract void cleanup(String corporateNumber, String parentMergeKey, String syncId);

    protected abstract String getCorporateNumber(T item);

    protected abstract String getParentMergeKey(T item);
}