package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.CommendationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommendationService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<CommendationDto> list) {
        for (CommendationDto dto : list) {
            mergeMapper.upsertCommendation(dto);
            mergeMapper.upsertCompanyCommendation(dto);
            runMapper.insertRunCommendation(
                    dto.getRunId(), dto.getCorporateNumber(), dto.getMergeKey());
            runMapper.insertRunCompany(dto.getRunId(), dto.getCorporateNumber());
        }
    }
}