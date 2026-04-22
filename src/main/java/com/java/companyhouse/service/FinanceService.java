package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.FinanceMapper;
import com.java.companyhouse.model.dto.FinanceDto;
import com.java.companyhouse.model.solved.JunctionPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FinanceService extends AbstractBatchService<FinanceDto> {

    static final String COMPANY_KEY = "company:id:";
    static final String FINANCE_KEY = "finance:id:";

    private final FinanceMapper financeMapper;
    private final CompanyMapper companyMapper;
    private final MajorShareholderService majorShareholderService;
    private final ManagementIndexService managementIndexService;
    private final Cache cache;

    public FinanceService(FinanceMapper financeMapper, CompanyMapper companyMapper, MajorShareholderService majorShareholderService, ManagementIndexService managementIndexService, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.financeMapper = financeMapper;
        this.companyMapper = companyMapper;
        this.majorShareholderService = majorShareholderService;
        this.managementIndexService = managementIndexService;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<FinanceDto> chunk) {
        financeMapper.bulkUpsertFinances(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(FINANCE_KEY, mergeKeys, financeMapper::findFinanceIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(FINANCE_KEY, mergeKeys, financeMapper::findFinanceIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
        financeMapper.bulkUpsertCompanyFinances(pairs, syncId);
    }

    @Override
    protected void cleanup(List<Long> companyIds, String syncId) {
        financeMapper.softDeleteMissingCompanyFinances(companyIds, syncId);
        majorShareholderService.deleteOrphanedFinanceShareholders(companyIds);
        managementIndexService.deleteOrphanedFinanceManagementIndexes(companyIds);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        List<Long> companyIds = new ArrayList<>(resolveCompanyIds(corporateNumbers).values());
        financeMapper.softDeleteAllCompanyFinances(companyIds);
        majorShareholderService.deleteOrphanedFinanceShareholders(companyIds);
        managementIndexService.deleteOrphanedFinanceManagementIndexes(companyIds);
    }

    @Override
    protected String getMergeKey(FinanceDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(FinanceDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected void warmEmptyCache(List<String> corporateNumbers) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }
}