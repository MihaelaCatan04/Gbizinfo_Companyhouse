package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CertificationMapper;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.model.solved.JunctionPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CertificationService extends AbstractBatchService<CertificationDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String CERTIFICATION_KEY = "certification:id:";

    private final CertificationMapper certificationMapper;
    private final CompanyMapper companyMapper;
    private final Cache cache;

    public CertificationService(CertificationMapper certificationMapper, CompanyMapper companyMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.certificationMapper = certificationMapper;
        this.companyMapper = companyMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<CertificationDto> chunk) {
        certificationMapper.bulkUpsertCertifications(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(CERTIFICATION_KEY, mergeKeys, certificationMapper::findCertificationIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(CERTIFICATION_KEY, mergeKeys, certificationMapper::findCertificationIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
        certificationMapper.bulkUpsertCompanyCertifications(pairs, syncId);
    }

    @Override
    protected void cleanup(List<Long> companyIds, String syncId) {
        certificationMapper.softDeleteMissingCompanyCertifications(companyIds, syncId);
    }

    @Override
    protected void warmEmptyCache(List<String> corporateNumbers) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        List<Long> companyIds = new ArrayList<>(resolveCompanyIds(corporateNumbers).values());
        certificationMapper.softDeleteAllCompanyCertifications(companyIds);
    }

    @Override
    protected String getMergeKey(CertificationDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(CertificationDto entity) {
        return entity.getCorporateNumber();
    }
}