package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.PatentMapper;
import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.model.solved.JunctionPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatentService extends AbstractBatchService<PatentDto> {

    static final String COMPANY_KEY = "company:id:";
    static final String PATENT_KEY = "patent:id:";

    private final PatentMapper patentMapper;
    private final CompanyMapper companyMapper;
    private final ClassificationService classificationService;
    private final Cache cache;

    public PatentService(PatentMapper patentMapper, CompanyMapper companyMapper, ClassificationService classificationService, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.patentMapper = patentMapper;
        this.companyMapper = companyMapper;
        this.classificationService = classificationService;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<PatentDto> chunk) {
        patentMapper.bulkUpsertPatents(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(PATENT_KEY, mergeKeys, patentMapper::findPatentIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(PATENT_KEY, mergeKeys, patentMapper::findPatentIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
        patentMapper.bulkUpsertCompanyPatents(pairs, syncId);
    }

    @Override
    protected void cleanup(List<Long> companyIds, String syncId) {
        patentMapper.softDeleteMissingCompanyPatents(companyIds, syncId);
        classificationService.deleteOrphanedPatentClassifications(companyIds);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        List<Long> companyIds = new ArrayList<>(resolveCompanyIds(corporateNumbers).values());
        patentMapper.softDeleteAllCompanyPatents(companyIds);
        classificationService.deleteOrphanedPatentClassifications(companyIds);
    }

    @Override
    protected String getMergeKey(PatentDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(PatentDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected void warmEmptyCache(List<String> corporateNumbers) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }
}