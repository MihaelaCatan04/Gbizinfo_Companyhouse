package com.java.companyhouse.service;

import com.java.companyhouse.mapper.WomenActivityMapper;
import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WomenActivityService extends AbstractBatchService<WomenActivityInfoDto> {

    private final WomenActivityMapper womenActivityMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<WomenActivityInfoDto> snapshot) {
        snapshot.getEntities().forEach(womenActivityMapper::upsertWomenActivity);
    }
}