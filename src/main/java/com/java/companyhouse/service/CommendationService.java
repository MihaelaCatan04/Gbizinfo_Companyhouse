package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CommendationMapper;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.model.dto.CommendationDto;
import com.java.companyhouse.model.solved.JunctionPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommendationService extends AbstractBatchService<CommendationDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String COMMENDATION_KEY = "commendation:id:";

    private final CommendationMapper commendationMapper;
    private final CompanyMapper companyMapper;
    private final Cache cache;

    public CommendationService(CommendationMapper commendationMapper, CompanyMapper companyMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.commendationMapper = commendationMapper;
        this.companyMapper = companyMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<CommendationDto> chunk) {
        commendationMapper.bulkUpsertCommendations(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(COMMENDATION_KEY, mergeKeys, commendationMapper::findCommendationIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(COMMENDATION_KEY, mergeKeys, commendationMapper::findCommendationIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
        commendationMapper.bulkUpsertCompanyCommendations(pairs, syncId);
    }

    @Override
    protected void cleanup(List<Long> companyIds, String syncId) {
        commendationMapper.softDeleteMissingCompanyCommendations(companyIds, syncId);
    }

    @Override
    protected void warmEmptyCache(List<String> corporateNumbers) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        List<Long> companyIds = new ArrayList<>(resolveCompanyIds(corporateNumbers).values());
        commendationMapper.softDeleteAllCompanyCommendations(companyIds);
    }

    @Override
    protected String getMergeKey(CommendationDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(CommendationDto entity) {
        return entity.getCorporateNumber();
    }
}