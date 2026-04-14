package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.WorkplaceInfoMapper;
import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

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
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        workplaceInfoMapper.clearCompanyWorkplaceInfo(corporateNumber);
    }

    @Override
    protected void upsert(WorkplaceInfoDto entity, String syncId) {
        workplaceInfoMapper.upsertWorkplaceInfo(entity);
        workplaceInfoMapper.upsertCompanyWorkplaceInfo(entity);
    }

    @Override
    protected String getCorporateNumber(WorkplaceInfoDto entity) {
        return entity.getCorporateNumber();
    }
}