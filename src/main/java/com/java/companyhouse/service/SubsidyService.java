package com.java.companyhouse.service;

import com.java.companyhouse.mapper.SubsidyMapper;
import com.java.companyhouse.model.dto.SubsidyDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubsidyService extends AbstractBatchService<SubsidyDto> {

    private final SubsidyMapper subsidyMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<SubsidyDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<SubsidyDto> subsidies = snapshot.getEntities();

        for (SubsidyDto dto : subsidies) {
            subsidyMapper.upsertSubsidy(dto);
            subsidyMapper.upsertCompanySubsidy(dto);
        }

        if (subsidies.isEmpty()) {
            subsidyMapper.softDeleteAllCompanySubsidies(corporateNumber);
            return;
        }

        List<String> mergeKeys = subsidies.stream().map(SubsidyDto::getMergeKey).toList();

        subsidyMapper.softDeleteMissingCompanySubsidies(corporateNumber, mergeKeys);
    }
}