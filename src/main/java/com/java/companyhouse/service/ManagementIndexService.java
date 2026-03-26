package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.ManagementIndexDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementIndexService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<ManagementIndexDto> list) {
        for (ManagementIndexDto dto : list) {
            mergeMapper.upsertManagementIndex(dto);
            mergeMapper.upsertFinanceManagement(dto);
            runMapper.insertRunManagementIndex(
                    dto.getRunId(), dto.getFinanceMergeKey(), dto.getMergeKey());
        }
    }
}