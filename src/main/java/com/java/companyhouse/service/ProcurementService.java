package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ProcurementMapper;
import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

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
    protected void processSnapshot(CompanySnapshot<ProcurementDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<ProcurementDto> procurements = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (procurements.isEmpty()) {
            deleteAllProcurements(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        upsertProcurements(procurements, syncId);
        cleanupMissingProcurements(corporateNumber, syncId);
    }

    private boolean isValidSnapshot(CompanySnapshot<ProcurementDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void deleteAllProcurements(String corporateNumber) {
        procurementMapper.softDeleteAllCompanyProcurements(corporateNumber);
    }

    private void upsertProcurements(List<ProcurementDto> procurements, String syncId) {
        for (ProcurementDto dto : procurements) {
            if (dto == null) continue;
            procurementMapper.upsertProcurement(dto);
            procurementMapper.upsertCompanyProcurement(dto, syncId);
        }
    }

    private void cleanupMissingProcurements(String corporateNumber, String syncId) {
        procurementMapper.softDeleteMissingCompanyProcurements(corporateNumber, syncId);
    }
}
