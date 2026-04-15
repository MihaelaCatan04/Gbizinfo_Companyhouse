package com.java.companyhouse.service;

import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.ProcurementIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ProcurementMapper;
import com.java.companyhouse.model.dto.ProcurementDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class ProcurementService extends AbstractBatchService<ProcurementDto> {

    private final ProcurementMapper procurementMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final ProcurementIdResolver procurementIdResolver;

    public ProcurementService(ProcurementMapper procurementMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, ProcurementIdResolver procurementIdResolver) {
        super(transactionTemplate);
        this.procurementMapper = procurementMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.procurementIdResolver = procurementIdResolver;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        procurementMapper.softDeleteAllCompanyProcurements(companyId);
    }

    @Override
    protected void upsert(ProcurementDto entity, String syncId) {
        procurementMapper.upsertProcurement(entity);

        Long companyId = companyIdResolver.resolve(entity.getCorporateNumber());
        Long procurementId = procurementIdResolver.resolve(entity.getMergeKey());

        procurementMapper.upsertCompanyProcurement(companyId, procurementId, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        procurementMapper.softDeleteMissingCompanyProcurements(companyId, syncId);
    }

    @Override
    protected String getCorporateNumber(ProcurementDto entity) {
        return entity.getCorporateNumber();
    }
}