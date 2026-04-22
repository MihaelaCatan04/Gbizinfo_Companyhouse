package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.FinanceMapper;
import com.java.companyhouse.mapper.MajorShareholderMapper;
import com.java.companyhouse.model.dto.MajorShareholderDto;
import com.java.companyhouse.model.receiver.FinanceMajorShareholderSnapshot;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.JunctionTriple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MajorShareholderService extends AbstractNestedBatchService<FinanceMajorShareholderSnapshot, MajorShareholderDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String FINANCE_KEY = "finance:id:";
    private static final String MAJOR_SHAREHOLDER_KEY = "major_shareholder:id:";

    private final MajorShareholderMapper majorShareholderMapper;
    private final CompanyMapper companyMapper;
    private final FinanceMapper financeMapper;
    private final Cache cache;

    public MajorShareholderService(MajorShareholderMapper majorShareholderMapper, CompanyMapper companyMapper, FinanceMapper financeMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.majorShareholderMapper = majorShareholderMapper;
        this.companyMapper = companyMapper;
        this.financeMapper = financeMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<MajorShareholderDto> chunk) {
        majorShareholderMapper.bulkUpsertMajorShareholders(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> parentMergeKeys, List<String> childMergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(FINANCE_KEY, parentMergeKeys, financeMapper::findFinanceIdsByMergeKeys);
        cache.warmAll(MAJOR_SHAREHOLDER_KEY, childMergeKeys, majorShareholderMapper::findMajorShareholderIdsByMergeKeys);
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
        return cache.resolveAll(MAJOR_SHAREHOLDER_KEY, mergeKeys, majorShareholderMapper::findMajorShareholderIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionTriple> triples, String syncId) {
        majorShareholderMapper.bulkUpsertFinanceShareholders(triples, syncId);
    }

    @Override
    protected void cleanup(List<JunctionPair> processedPairs, String syncId) {
        majorShareholderMapper.softDeleteMissingFinanceShareholders(processedPairs, syncId);
    }

    @Override
    protected void deleteAllBatch(List<FinanceMajorShareholderSnapshot> emptySnapshots) {
        Map<String, Long> companyIds = resolveCompanyIds(emptySnapshots.stream().map(FinanceMajorShareholderSnapshot::getCorporateNumber).distinct().toList());
        Map<String, Long> financeIds = resolveParentIds(emptySnapshots.stream().map(FinanceMajorShareholderSnapshot::getParentMergeKey).distinct().toList());

        List<JunctionPair> pairs = emptySnapshots.stream().map(s -> {
            Long companyId = companyIds.get(s.getCorporateNumber());
            Long financeId = financeIds.get(s.getParentMergeKey());
            return companyId != null && financeId != null ? new JunctionPair(companyId, financeId) : null;
        }).filter(Objects::nonNull).distinct().toList();

        majorShareholderMapper.softDeleteAllFinanceShareholders(pairs);
    }

    public void deleteOrphanedFinanceShareholders(List<Long> companyIds) {
        majorShareholderMapper.softDeleteOrphanedFinanceShareholders(companyIds);
    }

    @Override
    protected String getMergeKey(MajorShareholderDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(MajorShareholderDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected String getParentMergeKey(MajorShareholderDto entity) {
        return entity.getFinanceMergeKey();
    }
}