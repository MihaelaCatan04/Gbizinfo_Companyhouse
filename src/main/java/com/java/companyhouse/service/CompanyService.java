package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.model.dto.CompanyDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CompanyService extends AbstractBatchService<CompanyDto> {

    static final String COMPANY_KEY = "company:id:";

    private final CompanyMapper companyMapper;
    private final Cache cache;

    public CompanyService(CompanyMapper companyMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.companyMapper = companyMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<CompanyDto> chunk) {
        companyMapper.bulkUpsertCompanies(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected String getMergeKey(CompanyDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected String getCorporateNumber(CompanyDto entity) {
        return entity.getCorporateNumber();
    }
}