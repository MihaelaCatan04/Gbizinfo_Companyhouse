package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.SubsidyMapper;
import com.java.companyhouse.model.dto.SubsidyDto;
import com.java.companyhouse.model.solved.JunctionPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SubsidyService extends AbstractBatchService<SubsidyDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String SUBSIDY_KEY = "subsidy:id:";

    private final SubsidyMapper subsidyMapper;
    private final CompanyMapper companyMapper;
    private final Cache cache;

    public SubsidyService(SubsidyMapper subsidyMapper, CompanyMapper companyMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.subsidyMapper = subsidyMapper;
        this.companyMapper = companyMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<SubsidyDto> chunk) {
        subsidyMapper.bulkUpsertSubsidies(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(SUBSIDY_KEY, mergeKeys, subsidyMapper::findSubsidyIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(SUBSIDY_KEY, mergeKeys, subsidyMapper::findSubsidyIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
        subsidyMapper.bulkUpsertCompanySubsidies(pairs, syncId);
    }

    @Override
    protected void cleanup(List<Long> companyIds, String syncId) {
        subsidyMapper.softDeleteMissingCompanySubsidies(companyIds, syncId);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        List<Long> companyIds = new ArrayList<>(resolveCompanyIds(corporateNumbers).values());
        subsidyMapper.softDeleteAllCompanySubsidies(companyIds);
    }

    @Override
    protected String getMergeKey(SubsidyDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(SubsidyDto entity) {
        return entity.getCorporateNumber();
    }
}