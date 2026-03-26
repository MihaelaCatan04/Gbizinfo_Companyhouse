package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.ItemInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemInfoService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<ItemInfoDto> list) {
        for (ItemInfoDto dto : list) {
            mergeMapper.upsertItemInfo(dto);
            mergeMapper.upsertCompanyItem(dto);
            runMapper.insertRunCompanyItem(
                    dto.getRunId(), dto.getCorporateNumber(), dto.getMergeKey());
            runMapper.insertRunCompany(dto.getRunId(), dto.getCorporateNumber());
        }
    }
}