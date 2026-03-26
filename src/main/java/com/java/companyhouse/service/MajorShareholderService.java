package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.MajorShareholderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorShareholderService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<MajorShareholderDto> list) {
        for (MajorShareholderDto dto : list) {
            mergeMapper.upsertMajorShareholder(dto);
            mergeMapper.upsertFinanceShareholder(dto);
            runMapper.insertRunMajorShareholder(
                    dto.getRunId(), dto.getFinanceMergeKey(), dto.getMergeKey());
        }
    }
}