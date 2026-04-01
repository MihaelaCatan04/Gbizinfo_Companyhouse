package com.java.companyhouse.service;

import com.java.companyhouse.mapper.WorkplaceInfoMapper;
import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkplaceInfoService extends AbstractBatchService<WorkplaceInfoDto> {

    private final WorkplaceInfoMapper workplaceInfoMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<WorkplaceInfoDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<WorkplaceInfoDto> entities = snapshot.getEntities();

        if (entities.isEmpty()) {
            workplaceInfoMapper.clearCompanyWorkplaceInfo(corporateNumber);
            return;
        }

        for (WorkplaceInfoDto dto : entities) {
            workplaceInfoMapper.upsertWorkplaceInfo(dto);
            workplaceInfoMapper.upsertCompanyWorkplaceInfo(dto);
        }
    }
}