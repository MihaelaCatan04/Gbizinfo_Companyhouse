package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.PatentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatentService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<PatentDto> list) {
        for (PatentDto dto : list) {
            mergeMapper.upsertPatent(dto);
            mergeMapper.upsertCompanyPatent(dto);
            runMapper.insertRunPatent(dto.getRunId(), dto.getMergeKey());
            runMapper.insertRunCompany(dto.getRunId(), dto.getCorporateNumber());
        }
    }
}