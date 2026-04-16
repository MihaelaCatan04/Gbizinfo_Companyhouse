package com.java.companyhouse.service;

import com.java.companyhouse.cache.BaseInfoIdResolver;
import com.java.companyhouse.cache.CompatibilityIdResolver;
import com.java.companyhouse.cache.WomenActivityInfoIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.WorkplaceInfoMapper;
import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class WorkplaceInfoService extends AbstractBatchService<WorkplaceInfoDto> {

    private final WorkplaceInfoMapper workplaceInfoMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final BaseInfoIdResolver baseInfoIdResolver;
    private final WomenActivityInfoIdResolver womenActivityInfoIdResolver;
    private final CompatibilityIdResolver compatibilityIdResolver;

    public WorkplaceInfoService(WorkplaceInfoMapper workplaceInfoMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, BaseInfoIdResolver baseInfoIdResolver, WomenActivityInfoIdResolver womenActivityInfoIdResolver, CompatibilityIdResolver compatibilityIdResolver) {
        super(transactionTemplate);
        this.workplaceInfoMapper = workplaceInfoMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.baseInfoIdResolver = baseInfoIdResolver;
        this.womenActivityInfoIdResolver = womenActivityInfoIdResolver;
        this.compatibilityIdResolver = compatibilityIdResolver;
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
        Long baseInfoId = entity.getBaseInfoMergeKey() != null ? baseInfoIdResolver.resolve(entity.getBaseInfoMergeKey()) : null;

        Long womenActivityInfoId = entity.getWomenActivityMergeKey() != null ? womenActivityInfoIdResolver.resolve(entity.getWomenActivityMergeKey()) : null;

        Long compatibilityId = entity.getCompatibilityMergeKey() != null ? compatibilityIdResolver.resolve(entity.getCompatibilityMergeKey()) : null;

        workplaceInfoMapper.upsertWorkplaceInfo(entity.getMergeKey(), baseInfoId, womenActivityInfoId, compatibilityId);

        String current = workplaceInfoMapper.findCompanyWorkplaceInfo(entity.getCorporateNumber());
        if (!entity.getMergeKey().equals(current)) {
            workplaceInfoMapper.upsertCompanyWorkplaceInfo(entity.getMergeKey(), entity.getCorporateNumber());
        }
    }

    @Override
    protected String getCorporateNumber(WorkplaceInfoDto entity) {
        return entity.getCorporateNumber();
    }
}