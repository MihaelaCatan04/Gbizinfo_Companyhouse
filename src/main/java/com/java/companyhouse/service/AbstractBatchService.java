package com.java.companyhouse.service;

import com.java.companyhouse.model.receiver.CompanySnapshot;
import com.java.companyhouse.model.receiver.TopicBatchRequest;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.PhaseTwoData;
import com.java.companyhouse.model.solved.SnapshotGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractBatchService<E> {

    private final TransactionTemplate transactionTemplate;
    private final int batchSize;

    static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            result.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return result;
    }

    public void receive(TopicBatchRequest<E> request) {
        if (request == null || request.getCompanies() == null) return;

        List<CompanySnapshot<E>> snapshots = request.getCompanies().stream().filter(s -> s != null && s.getCorporateNumber() != null).toList();

        if (snapshots.isEmpty()) return;

        String syncId = UUID.randomUUID().toString();
        phaseOne(snapshots);
        phaseTwo(snapshots, syncId);
    }

    private void phaseOne(List<CompanySnapshot<E>> snapshots) {
        List<E> all = collectAndValidateEntities(snapshots);
        if (all.isEmpty()) return;

        List<E> deduped = deduplicate(all);
        List<E> sorted = sortByMergeKey(deduped);

        for (List<E> chunk : partition(sorted, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> bulkUpsertEntities(chunk));
        }
    }

    private List<E> collectAndValidateEntities(List<CompanySnapshot<E>> snapshots) {
        List<E> all = new ArrayList<>();
        for (CompanySnapshot<E> snapshot : snapshots) {
            for (E entity : safeEntities(snapshot)) {
                if (entity == null) continue;
                validateEntity(snapshot, entity);
                all.add(entity);
            }
        }
        return all;
    }

    private void validateEntity(CompanySnapshot<E> snapshot, E entity) {
        String entityCn = getCorporateNumber(entity);
        if (entityCn != null && !entityCn.equals(snapshot.getCorporateNumber())) {
            throw new IllegalArgumentException("Entity corporateNumber " + entityCn + " does not match snapshot corporateNumber " + snapshot.getCorporateNumber());
        }
    }

    private List<E> deduplicate(List<E> all) {
        Map<String, E> deduped = new LinkedHashMap<>();
        for (E entity : all) {
            String mk = getMergeKey(entity);
            if (mk != null) deduped.putIfAbsent(mk, entity);
        }
        return new ArrayList<>(deduped.values());
    }

    private List<E> sortByMergeKey(List<E> entities) {
        entities.sort(Comparator.comparing(this::getMergeKey));
        return entities;
    }

    private void phaseTwo(List<CompanySnapshot<E>> snapshots, String syncId) {
        SnapshotGroups<CompanySnapshot<E>> groups = splitSnapshots(snapshots);

        handleEmptySnapshots(groups.getEmpty());

        if (groups.getNonEmpty().isEmpty()) return;

        warmPhaseTwoCache(groups.getNonEmpty());

        PhaseTwoData<JunctionPair, Long> phaseTwoData = buildPhaseTwoData(groups.getNonEmpty());

        upsertJunctionPairs(phaseTwoData.getRelations(), syncId);
        cleanupProcessedCompanies(phaseTwoData.getProcessed(), syncId);
    }

    private SnapshotGroups<CompanySnapshot<E>> splitSnapshots(List<CompanySnapshot<E>> snapshots) {
        List<CompanySnapshot<E>> empty = new ArrayList<>();
        List<CompanySnapshot<E>> nonEmpty = new ArrayList<>();

        for (CompanySnapshot<E> snapshot : snapshots) {
            if (hasValidEntities(snapshot)) {
                nonEmpty.add(snapshot);
            } else {
                empty.add(snapshot);
            }
        }

        return new SnapshotGroups<>(empty, nonEmpty);
    }

    private boolean hasValidEntities(CompanySnapshot<E> snapshot) {
        return safeEntities(snapshot).stream().anyMatch(entity -> entity != null && getMergeKey(entity) != null);
    }

    private List<E> safeEntities(CompanySnapshot<E> snapshot) {
        return snapshot.getEntities() == null ? Collections.emptyList() : snapshot.getEntities();
    }

    private void handleEmptySnapshots(List<CompanySnapshot<E>> emptySnapshots) {
        if (emptySnapshots.isEmpty()) return;

        List<String> corporateNumbers = emptySnapshots.stream().map(CompanySnapshot::getCorporateNumber).distinct().collect(Collectors.toList());

        warmEmptyCache(corporateNumbers);

        for (List<String> chunk : partition(corporateNumbers, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> onEmptyBatch(chunk));
        }
    }

    protected void onEmptyBatch(List<String> corporateNumbers) {
    }

    protected void warmEmptyCache(List<String> corporateNumbers) {
    }

    private void warmPhaseTwoCache(List<CompanySnapshot<E>> nonEmptySnapshots) {
        List<String> corporateNumbers = nonEmptySnapshots.stream().map(CompanySnapshot::getCorporateNumber).distinct().collect(Collectors.toList());

        List<String> mergeKeys = nonEmptySnapshots.stream().flatMap(snapshot -> safeEntities(snapshot).stream()).filter(entity -> entity != null && getMergeKey(entity) != null).map(this::getMergeKey).distinct().collect(Collectors.toList());

        warmCache(corporateNumbers, mergeKeys);
    }

    private PhaseTwoData<JunctionPair, Long> buildPhaseTwoData(List<CompanySnapshot<E>> nonEmptySnapshots) {
        List<String> corporateNumbers = extractCorporateNumbers(nonEmptySnapshots);
        List<String> childMergeKeys = extractChildMergeKeys(nonEmptySnapshots);

        Map<String, Long> companyIds = resolveCompanyIds(corporateNumbers);
        Map<String, Long> childIds = resolveChildIds(childMergeKeys);

        List<JunctionPair> pairs = buildPairs(nonEmptySnapshots, companyIds, childIds);
        List<Long> processedCompanyIds = extractProcessedCompanyIds(nonEmptySnapshots, companyIds);

        List<JunctionPair> dedupedPairs = dedupeAndSortPairs(pairs);

        return new PhaseTwoData<>(dedupedPairs, processedCompanyIds);
    }

    private List<String> extractCorporateNumbers(List<CompanySnapshot<E>> snapshots) {
        return snapshots.stream().map(CompanySnapshot::getCorporateNumber).distinct().collect(Collectors.toList());
    }

    private List<String> extractChildMergeKeys(List<CompanySnapshot<E>> snapshots) {
        return snapshots.stream().flatMap(snapshot -> safeEntities(snapshot).stream()).filter(entity -> entity != null && getMergeKey(entity) != null).map(this::getMergeKey).distinct().collect(Collectors.toList());
    }

    private List<JunctionPair> buildPairs(List<CompanySnapshot<E>> snapshots, Map<String, Long> companyIds, Map<String, Long> childIds) {
        List<JunctionPair> pairs = new ArrayList<>();
        for (CompanySnapshot<E> snapshot : snapshots) {
            Long companyId = companyIds.get(snapshot.getCorporateNumber());
            if (companyId == null) continue;

            for (E entity : safeEntities(snapshot)) {
                if (entity == null) continue;
                String mergeKey = getMergeKey(entity);
                if (mergeKey == null) continue;

                Long childId = childIds.get(mergeKey);
                if (childId == null) continue;

                pairs.add(new JunctionPair(companyId, childId));
            }
        }
        return pairs;
    }

    private List<Long> extractProcessedCompanyIds(List<CompanySnapshot<E>> snapshots, Map<String, Long> companyIds) {
        return snapshots.stream().map(s -> companyIds.get(s.getCorporateNumber())).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    private List<JunctionPair> dedupeAndSortPairs(List<JunctionPair> pairs) {
        return pairs.stream().distinct().sorted(Comparator.comparingLong(JunctionPair::getCompanyId).thenComparingLong(JunctionPair::getChildId)).collect(Collectors.toList());
    }

    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return Collections.emptyMap();
    }

    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return Collections.emptyMap();
    }

    private void upsertJunctionPairs(List<JunctionPair> pairs, String syncId) {
        for (List<JunctionPair> chunk : partition(pairs, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> bulkUpsertJunction(chunk, syncId));
        }
    }

    private void cleanupProcessedCompanies(List<Long> processedCompanyIds, String syncId) {
        if (processedCompanyIds.isEmpty()) return;
        for (List<Long> chunk : partition(processedCompanyIds, batchSize)) {
            transactionTemplate.executeWithoutResult(status -> cleanup(chunk, syncId));
        }
    }


    protected abstract String getMergeKey(E entity);

    protected abstract String getCorporateNumber(E entity);

    protected abstract void warmCache(List<String> corporateNumbers, List<String> mergeKeys);

    protected abstract void bulkUpsertEntities(List<E> chunk);

    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
    }

    protected void cleanup(List<Long> companyIds, String syncId) {
    }
}