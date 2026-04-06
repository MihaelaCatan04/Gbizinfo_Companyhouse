package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.WorkplaceInfoMapper;
import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class WorkplaceInfoService extends AbstractBatchService<WorkplaceInfoDto> {

    private final WorkplaceInfoMapper workplaceInfoMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public WorkplaceInfoService(WorkplaceInfoMapper workplaceInfoMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.workplaceInfoMapper = workplaceInfoMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void processSnapshot(CompanySnapshot<WorkplaceInfoDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<WorkplaceInfoDto> entities = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (entities.isEmpty()) {
            clearWorkplaceInfo(corporateNumber);
            return;
        }

        upsertWorkplaceInfos(entities);
    }

    private boolean isValidSnapshot(CompanySnapshot<WorkplaceInfoDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void clearWorkplaceInfo(String corporateNumber) {
        workplaceInfoMapper.clearCompanyWorkplaceInfo(corporateNumber);
    }

    private void upsertWorkplaceInfos(List<WorkplaceInfoDto> entities) {
        for (WorkplaceInfoDto dto : entities) {
            if (dto == null) continue;
            workplaceInfoMapper.upsertWorkplaceInfo(dto);
            workplaceInfoMapper.upsertCompanyWorkplaceInfo(dto);
        }
    }
}
