package com.java.companyhouse.service;

import com.java.companyhouse.mapper.BaseInfoMapper;
import com.java.companyhouse.model.dto.BaseInfoDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseInfoService extends AbstractBatchService<BaseInfoDto> {

    private final BaseInfoMapper baseInfoMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<BaseInfoDto> snapshot) {
        snapshot.getEntities().forEach(baseInfoMapper::upsertBaseInfo);
    }
}