package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.SubsidyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubsidyService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<SubsidyDto> list) {
        for (SubsidyDto dto : list) {
            mergeMapper.upsertSubsidy(dto);
            mergeMapper.upsertCompanySubsidy(dto);
            runMapper.insertRunSubsidy(
                    dto.getRunId(), dto.getCorporateNumber(), dto.getMergeKey());
            runMapper.insertRunCompany(dto.getRunId(), dto.getCorporateNumber());
        }
    }
}