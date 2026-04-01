package com.java.companyhouse.service;

import com.java.companyhouse.mapper.SubsidyMapper;
import com.java.companyhouse.model.dto.SubsidyDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubsidyService extends AbstractBatchService<SubsidyDto> {

    private final SubsidyMapper subsidyMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<SubsidyDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<SubsidyDto> subsidies = snapshot.getEntities();

        if (subsidies.isEmpty()) {
            subsidyMapper.softDeleteAllCompanySubsidies(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        for (SubsidyDto dto : subsidies) {
            subsidyMapper.upsertSubsidy(dto);
            subsidyMapper.upsertCompanySubsidy(dto, syncId);
        }

        subsidyMapper.softDeleteMissingCompanySubsidies(corporateNumber, syncId);
    }
}