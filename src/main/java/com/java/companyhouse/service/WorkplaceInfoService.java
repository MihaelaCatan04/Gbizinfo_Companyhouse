package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkplaceInfoService {

    private final MergeMapper mergeMapper;

    @Transactional
    public void receive(List<WorkplaceInfoDto> list) {
        for (WorkplaceInfoDto dto : list) {
            mergeMapper.upsertWorkplaceInfo(dto);
            mergeMapper.upsertCompanyWorkplaceInfo(dto);
        }
    }
}