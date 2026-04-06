package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.BaseInfoMapper;
import com.java.companyhouse.model.dto.BaseInfoDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class BaseInfoService extends AbstractBatchService<BaseInfoDto> {

    private final BaseInfoMapper baseInfoMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public BaseInfoService(BaseInfoMapper baseInfoMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.baseInfoMapper = baseInfoMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void processSnapshot(CompanySnapshot<BaseInfoDto> snapshot) {
        advisoryLockMapper.acquireAdvisoryLock(snapshot.getCorporateNumber());
        snapshot.getEntities().forEach(baseInfoMapper::upsertBaseInfo);
    }
}