package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.ProcurementMapper;
import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.model.solved.JunctionPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProcurementService extends AbstractBatchService<ProcurementDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String PROCUREMENT_KEY = "procurement:id:";

    private final ProcurementMapper procurementMapper;
    private final CompanyMapper companyMapper;
    private final Cache cache;

    public ProcurementService(ProcurementMapper procurementMapper, CompanyMapper companyMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.procurementMapper = procurementMapper;
        this.companyMapper = companyMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<ProcurementDto> chunk) {
        procurementMapper.bulkUpsertProcurements(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(PROCUREMENT_KEY, mergeKeys, procurementMapper::findProcurementIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(PROCUREMENT_KEY, mergeKeys, procurementMapper::findProcurementIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
        procurementMapper.bulkUpsertCompanyProcurements(pairs, syncId);
    }

    @Override
    protected void cleanup(List<Long> companyIds, String syncId) {
        procurementMapper.softDeleteMissingCompanyProcurements(companyIds, syncId);
    }

    @Override
    protected void warmEmptyCache(List<String> corporateNumbers) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        List<Long> companyIds = new ArrayList<>(resolveCompanyIds(corporateNumbers).values());
        procurementMapper.softDeleteAllCompanyProcurements(companyIds);
    }

    @Override
    protected String getMergeKey(ProcurementDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(ProcurementDto entity) {
        return entity.getCorporateNumber();
    }
}