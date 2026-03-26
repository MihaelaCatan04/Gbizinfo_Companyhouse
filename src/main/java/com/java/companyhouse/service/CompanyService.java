package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final RunMapper runMapper;
    private final MergeMapper mergeMapper;

    @Transactional
    public void receive(List<CompanyDto> companies) {
        for (CompanyDto dto : companies) {
            mergeMapper.upsertCompany(dto);
            runMapper.insertRunCompany(dto.getRunId(), dto.getCorporateNumber());
        }
    }
}