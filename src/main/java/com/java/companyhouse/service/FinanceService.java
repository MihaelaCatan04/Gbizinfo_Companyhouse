package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.FinanceMapper;
import com.java.companyhouse.model.dto.FinanceDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class FinanceService extends AbstractBatchService<FinanceDto> {

    private final FinanceMapper financeMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public FinanceService(FinanceMapper financeMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.financeMapper = financeMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void processSnapshot(CompanySnapshot<FinanceDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<FinanceDto> finances = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (finances.isEmpty()) {
            deleteAllFinances(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        upsertFinances(finances, syncId);
        cleanupMissingFinances(corporateNumber, syncId);
    }

    private boolean isValidSnapshot(CompanySnapshot<FinanceDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void deleteAllFinances(String corporateNumber) {
        financeMapper.softDeleteAllCompanyFinances(corporateNumber);
    }

    private void upsertFinances(List<FinanceDto> finances, String syncId) {
        for (FinanceDto dto : finances) {
            if (dto == null) continue;
            financeMapper.upsertFinance(dto);
            financeMapper.upsertCompanyFinance(dto, syncId);
        }
    }

    private void cleanupMissingFinances(String corporateNumber, String syncId) {
        financeMapper.softDeleteMissingCompanyFinances(corporateNumber, syncId);
    }
}
