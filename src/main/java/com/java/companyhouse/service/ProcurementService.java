package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.ProcurementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcurementService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<ProcurementDto> list) {
        for (ProcurementDto dto : list) {
            mergeMapper.upsertProcurement(dto);
            mergeMapper.upsertCompanyProcurement(dto);
            runMapper.insertRunProcurement(
                    dto.getRunId(), dto.getCorporateNumber(), dto.getMergeKey());
            runMapper.insertRunCompany(dto.getRunId(), dto.getCorporateNumber());
        }
    }
}