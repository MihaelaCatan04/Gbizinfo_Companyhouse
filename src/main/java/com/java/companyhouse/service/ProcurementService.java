package com.java.companyhouse.service;

import com.java.companyhouse.mapper.ProcurementMapper;
import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcurementService extends AbstractBatchService<ProcurementDto> {

    private final ProcurementMapper procurementMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<ProcurementDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<ProcurementDto> procurements = snapshot.getEntities();

        if (procurements.isEmpty()) {
            procurementMapper.softDeleteAllCompanyProcurements(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        for (ProcurementDto dto : procurements) {
            procurementMapper.upsertProcurement(dto);
            procurementMapper.upsertCompanyProcurement(dto, syncId);
        }

        procurementMapper.softDeleteMissingCompanyProcurements(corporateNumber, syncId);
    }
}