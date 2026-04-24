package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.FinanceMapper;
import com.java.companyhouse.mapper.ManagementIndexMapper;
import com.java.companyhouse.model.dto.ManagementIndexDto;
import com.java.companyhouse.model.receiver.FinanceManagementIndexSnapshot;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.JunctionTriple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ManagementIndexService extends AbstractNestedBatchService<FinanceManagementIndexSnapshot, ManagementIndexDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String FINANCE_KEY = "finance:id:";
    private static final String MANAGEMENT_INDEX_KEY = "management_index:id:";

    private final ManagementIndexMapper managementIndexMapper;
    private final CompanyMapper companyMapper;
    private final FinanceMapper financeMapper;
    private final Cache cache;

    public ManagementIndexService(ManagementIndexMapper managementIndexMapper, CompanyMapper companyMapper, FinanceMapper financeMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.managementIndexMapper = managementIndexMapper;
        this.companyMapper = companyMapper;
        this.financeMapper = financeMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<ManagementIndexDto> chunk) {
        managementIndexMapper.bulkUpsertManagementIndexes(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> parentMergeKeys, List<String> childMergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(FINANCE_KEY, parentMergeKeys, financeMapper::findFinanceIdsByMergeKeys);
        cache.warmAll(MANAGEMENT_INDEX_KEY, childMergeKeys, managementIndexMapper::findManagementIndexIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveParentIds(List<String> parentMergeKeys) {
        return cache.resolveAll(FINANCE_KEY, parentMergeKeys, financeMapper::findFinanceIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(MANAGEMENT_INDEX_KEY, mergeKeys, managementIndexMapper::findManagementIndexIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionTriple> triples, String syncId) {
        managementIndexMapper.bulkUpsertFinanceManagementIndexes(triples, syncId);
    }

    @Override
    protected void cleanup(List<JunctionPair> processedPairs, String syncId) {
        managementIndexMapper.softDeleteMissingFinanceManagementIndexes(processedPairs, syncId);
    }

    @Override
    protected void deleteAllBatch(List<FinanceManagementIndexSnapshot> emptySnapshots) {
        Map<String, Long> companyIds = resolveCompanyIds(emptySnapshots.stream().map(FinanceManagementIndexSnapshot::getCorporateNumber).distinct().toList());
        Map<String, Long> financeIds = resolveParentIds(emptySnapshots.stream().map(FinanceManagementIndexSnapshot::getParentMergeKey).distinct().toList());

        List<JunctionPair> pairs = emptySnapshots.stream().map(s -> {
            Long companyId = companyIds.get(s.getCorporateNumber());
            Long financeId = financeIds.get(s.getParentMergeKey());
            return companyId != null && financeId != null ? new JunctionPair(companyId, financeId) : null;
        }).filter(Objects::nonNull).distinct().toList();

        managementIndexMapper.softDeleteAllFinanceManagementIndexes(pairs);
    }

    public void deleteOrphanedFinanceManagementIndexes(List<Long> companyIds) {
        managementIndexMapper.softDeleteOrphanedFinanceManagementIndexes(companyIds);
    }

    @Override
    protected String getMergeKey(ManagementIndexDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(ManagementIndexDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected String getParentMergeKey(ManagementIndexDto entity) {
        return entity.getFinanceMergeKey();
    }
}