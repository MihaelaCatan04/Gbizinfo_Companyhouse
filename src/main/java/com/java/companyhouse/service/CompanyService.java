package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.model.dto.CompanyDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CompanyService extends AbstractBatchService<CompanyDto> {

    private final CompanyMapper companyMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public CompanyService(CompanyMapper companyMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.companyMapper = companyMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void processSnapshot(CompanySnapshot<CompanyDto> snapshot) {
        advisoryLockMapper.acquireAdvisoryLock(snapshot.getCorporateNumber());
        snapshot.getEntities().forEach(companyMapper::upsertCompany);
    }
}