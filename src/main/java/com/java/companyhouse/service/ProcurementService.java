package com.java.companyhouse.service;

import com.java.companyhouse.mapper.ProcurementMapper;
import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcurementService extends AbstractBatchService<ProcurementDto> {

    private final ProcurementMapper procurementMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<ProcurementDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<ProcurementDto> procurements = snapshot.getEntities();

        for (ProcurementDto dto : procurements) {
            procurementMapper.upsertProcurement(dto);
            procurementMapper.upsertCompanyProcurement(dto);
        }

        if (procurements.isEmpty()) {
            procurementMapper.softDeleteAllCompanyProcurements(corporateNumber);
            return;
        }

        List<String> mergeKeys = procurements.stream().map(ProcurementDto::getMergeKey).toList();

        procurementMapper.softDeleteMissingCompanyProcurements(corporateNumber, mergeKeys);
    }
}