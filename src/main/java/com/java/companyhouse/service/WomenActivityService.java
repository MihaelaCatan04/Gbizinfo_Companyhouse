package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.WomenActivityMapper;
import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class WomenActivityService extends AbstractBatchService<WomenActivityInfoDto> {

    private final WomenActivityMapper womenActivityMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public WomenActivityService(WomenActivityMapper womenActivityMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.womenActivityMapper = womenActivityMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void upsert(WomenActivityInfoDto entity, String syncId) {
        womenActivityMapper.upsertWomenActivity(entity);
    }
}