package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ProcurementMapper;
import com.java.companyhouse.model.dto.ProcurementDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class ProcurementService extends AbstractBatchService<ProcurementDto> {

    private final ProcurementMapper procurementMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public ProcurementService(ProcurementMapper procurementMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.procurementMapper = procurementMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        procurementMapper.softDeleteAllCompanyProcurements(corporateNumber);
    }

    @Override
    protected void upsert(ProcurementDto entity, String syncId) {
        procurementMapper.upsertProcurement(entity);
        procurementMapper.upsertCompanyProcurement(entity, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        procurementMapper.softDeleteMissingCompanyProcurements(corporateNumber, syncId);
    }

    @Override
    protected String getCorporateNumber(ProcurementDto entity) {
        return entity.getCorporateNumber();
    }
}