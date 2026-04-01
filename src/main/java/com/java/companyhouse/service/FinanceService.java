package com.java.companyhouse.service;

import com.java.companyhouse.mapper.FinanceMapper;
import com.java.companyhouse.model.dto.FinanceDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinanceService extends AbstractBatchService<FinanceDto> {

    private final FinanceMapper financeMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<FinanceDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<FinanceDto> finances = snapshot.getEntities();

        if (finances.isEmpty()) {
            financeMapper.softDeleteAllCompanyFinances(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        for (FinanceDto dto : finances) {
            financeMapper.upsertFinance(dto);
            financeMapper.upsertCompanyFinance(dto, syncId);
        }

        financeMapper.softDeleteMissingCompanyFinances(corporateNumber, syncId);
    }
}