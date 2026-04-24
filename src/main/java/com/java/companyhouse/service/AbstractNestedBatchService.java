package com.java.companyhouse.service;

import com.java.companyhouse.model.receiver.NestedBatchRequest;
import com.java.companyhouse.model.receiver.NestedSnapshot;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.JunctionTriple;
import com.java.companyhouse.model.solved.PhaseTwoData;
import com.java.companyhouse.model.solved.SnapshotGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractNestedBatchService<S extends NestedSnapshot<T>, T> {

    private final TransactionTemplate transactionTemplate;
    private final int batchSize;

    public void receive(NestedBatchRequest<S> request) {
        if (request == null || request.getSnapshots() == null) return;

        List<S> snapshots = request.getSnapshots().stream().filter(snapshot -> snapshot != null && snapshot.getCorporateNumber() != null && snapshot.getParentMergeKey() != null).toList();

        if (snapshots.isEmpty()) return;

        String syncId = UUID.randomUUID().toString();
        phaseOne(snapshots);
        phaseTwo(snapshots, syncId);
    }

    private void phaseOne(List<S> snapshots) {
        List<T> allItems = collectAndValidateItems(snapshots);
        if (allItems.isEmpty()) return;

        List<T> deduped = deduplicateItems(allItems);
        List<T> sorted = sortItemsByMergeKey(deduped);

        for (List<T> chunk : AbstractBatchService.partition(sorted, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> bulkUpsertEntities(chunk));
        }
    }

    private List<T> collectAndValidateItems(List<S> snapshots) {
        List<T> allItems = new ArrayList<>();
        for (S snapshot : snapshots) {
            for (T item : safeItems(snapshot)) {
                if (item == null) continue;
                validateItem(snapshot, item);
                allItems.add(item);
            }
        }
        return allItems;
    }

    private void validateItem(S snapshot, T item) {
        String itemCn = getCorporateNumber(item);
        String itemParentMergeKey = getParentMergeKey(item);

        if (itemCn != null && !itemCn.equals(snapshot.getCorporateNumber())) {
            throw new IllegalArgumentException("Item corporateNumber " + itemCn + " does not match snapshot " + snapshot.getCorporateNumber());
        }

        if (itemParentMergeKey != null && !itemParentMergeKey.equals(snapshot.getParentMergeKey())) {
            throw new IllegalArgumentException("Item parentMergeKey " + itemParentMergeKey + " does not match snapshot " + snapshot.getParentMergeKey());
        }
    }

    private List<T> deduplicateItems(List<T> items) {
        Map<String, T> deduped = new LinkedHashMap<>();
        for (T item : items) {
            String mk = getMergeKey(item);
            if (mk != null) deduped.putIfAbsent(mk, item);
        }
        return new ArrayList<>(deduped.values());
    }

    private List<T> sortItemsByMergeKey(List<T> items) {
        items.sort(Comparator.comparing(this::getMergeKey));
        return items;
    }

    private void phaseTwo(List<S> snapshots, String syncId) {
        SnapshotGroups<S> groups = splitSnapshots(snapshots);

        handleEmptySnapshots(groups.getEmpty());

        if (groups.getNonEmpty().isEmpty()) return;

        warmPhaseTwoCache(groups.getNonEmpty());

        PhaseTwoData<JunctionTriple, JunctionPair> phaseTwoData = buildPhaseTwoData(groups.getNonEmpty());

        upsertJunctionTriples(phaseTwoData.getRelations(), syncId);
        cleanupProcessedPairs(phaseTwoData.getProcessed(), syncId);
    }

    private SnapshotGroups<S> splitSnapshots(List<S> snapshots) {
        List<S> empty = new ArrayList<>();
        List<S> nonEmpty = new ArrayList<>();

        for (S snapshot : snapshots) {
            if (hasValidItems(snapshot)) {
                nonEmpty.add(snapshot);
            } else {
                empty.add(snapshot);
            }
        }

        return new SnapshotGroups<>(empty, nonEmpty);
    }

    private boolean hasValidItems(S snapshot) {
        return safeItems(snapshot).stream().anyMatch(item -> item != null && getMergeKey(item) != null);
    }

    private List<T> safeItems(S snapshot) {
        return snapshot.getItems() == null ? Collections.emptyList() : snapshot.getItems();
    }

    private void handleEmptySnapshots(List<S> emptySnapshots) {
        if (emptySnapshots.isEmpty()) return;

        List<String> corporateNumbers = emptySnapshots.stream().map(S::getCorporateNumber).distinct().collect(Collectors.toList());

        List<String> parentMergeKeys = emptySnapshots.stream().map(S::getParentMergeKey).distinct().collect(Collectors.toList());

        warmEmptyCache(corporateNumbers, parentMergeKeys);

        for (List<S> chunk : AbstractBatchService.partition(emptySnapshots, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> deleteAllBatch(chunk));
        }
    }

    protected void warmEmptyCache(List<String> corporateNumbers, List<String> parentMergeKeys) {
    }

    protected abstract void deleteAllBatch(List<S> emptySnapshots);

    private void warmPhaseTwoCache(List<S> nonEmptySnapshots) {
        List<String> corporateNumbers = nonEmptySnapshots.stream().map(S::getCorporateNumber).distinct().collect(Collectors.toList());

        List<String> parentMergeKeys = nonEmptySnapshots.stream().map(S::getParentMergeKey).distinct().collect(Collectors.toList());

        List<String> childMergeKeys = nonEmptySnapshots.stream().flatMap(snapshot -> safeItems(snapshot).stream()).filter(item -> item != null && getMergeKey(item) != null).map(this::getMergeKey).distinct().collect(Collectors.toList());

        warmCache(corporateNumbers, parentMergeKeys, childMergeKeys);
    }

    private PhaseTwoData<JunctionTriple, JunctionPair> buildPhaseTwoData(List<S> nonEmptySnapshots) {
        List<String> corporateNumbers = extractCorporateNumbers(nonEmptySnapshots);
        List<String> parentMergeKeys = extractParentMergeKeys(nonEmptySnapshots);
        List<String> childMergeKeys = extractChildMergeKeys(nonEmptySnapshots);

        Map<String, Long> companyIds = resolveCompanyIds(corporateNumbers);
        Map<String, Long> parentIds = resolveParentIds(parentMergeKeys);
        Map<String, Long> childIds = resolveChildIds(childMergeKeys);

        List<JunctionTriple> triples = buildTriples(nonEmptySnapshots, companyIds, parentIds, childIds);
        List<JunctionPair> processedPairs = buildProcessedPairs(nonEmptySnapshots, companyIds, parentIds);

        List<JunctionTriple> dedupedTriples = dedupeAndSortTriples(triples);
        List<JunctionPair> dedupedPairs = dedupePairs(processedPairs);

        return new PhaseTwoData<>(dedupedTriples, dedupedPairs);
    }

    private List<String> extractCorporateNumbers(List<S> snapshots) {
        return snapshots.stream().map(S::getCorporateNumber).distinct().collect(Collectors.toList());
    }

    private List<String> extractParentMergeKeys(List<S> snapshots) {
        return snapshots.stream().map(S::getParentMergeKey).distinct().collect(Collectors.toList());
    }

    private List<String> extractChildMergeKeys(List<S> snapshots) {
        return snapshots.stream().flatMap(snapshot -> safeItems(snapshot).stream()).filter(item -> item != null && getMergeKey(item) != null).map(this::getMergeKey).distinct().collect(Collectors.toList());
    }

    private List<JunctionTriple> buildTriples(List<S> snapshots, Map<String, Long> companyIds, Map<String, Long> parentIds, Map<String, Long> childIds) {
        List<JunctionTriple> triples = new ArrayList<>();
        for (S snapshot : snapshots) {
            Long companyId = companyIds.get(snapshot.getCorporateNumber());
            Long parentId = parentIds.get(snapshot.getParentMergeKey());
            if (companyId == null || parentId == null) continue;

            for (T item : safeItems(snapshot)) {
                if (item == null) continue;
                String mergeKey = getMergeKey(item);
                if (mergeKey == null) continue;

                Long childId = childIds.get(mergeKey);
                if (childId == null) continue;

                triples.add(new JunctionTriple(companyId, parentId, childId));
            }
        }
        return triples;
    }

    private List<JunctionPair> buildProcessedPairs(List<S> snapshots, Map<String, Long> companyIds, Map<String, Long> parentIds) {
        List<JunctionPair> pairs = new ArrayList<>();
        for (S snapshot : snapshots) {
            Long companyId = companyIds.get(snapshot.getCorporateNumber());
            Long parentId = parentIds.get(snapshot.getParentMergeKey());
            if (companyId == null || parentId == null) continue;
            pairs.add(new JunctionPair(companyId, parentId));
        }
        return pairs;
    }

    private List<JunctionTriple> dedupeAndSortTriples(List<JunctionTriple> triples) {
        return triples.stream().distinct().sorted(Comparator.comparingLong(JunctionTriple::getCompanyId).thenComparingLong(JunctionTriple::getParentId).thenComparingLong(JunctionTriple::getChildId)).collect(Collectors.toList());
    }

    private List<JunctionPair> dedupePairs(List<JunctionPair> pairs) {
        return pairs.stream().distinct().collect(Collectors.toList());
    }

    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return Collections.emptyMap();
    }

    protected Map<String, Long> resolveParentIds(List<String> parentMergeKeys) {
        return Collections.emptyMap();
    }

    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return Collections.emptyMap();
    }

    private void upsertJunctionTriples(List<JunctionTriple> triples, String syncId) {
        for (List<JunctionTriple> chunk : AbstractBatchService.partition(triples, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> bulkUpsertJunction(chunk, syncId));
        }
    }

    private void cleanupProcessedPairs(List<JunctionPair> processedPairs, String syncId) {
        if (processedPairs.isEmpty()) return;
        for (List<JunctionPair> chunk : AbstractBatchService.partition(processedPairs, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> cleanup(chunk, syncId));
        }
    }

    protected abstract String getMergeKey(T item);

    protected abstract String getCorporateNumber(T item);

    protected abstract String getParentMergeKey(T item);

    protected abstract void warmCache(List<String> corporateNumbers, List<String> parentMergeKeys, List<String> childMergeKeys);

    protected abstract void bulkUpsertEntities(List<T> chunk);

    protected abstract void bulkUpsertJunction(List<JunctionTriple> triples, String syncId);

    protected abstract void cleanup(List<JunctionPair> processedPairs, String syncId);
}