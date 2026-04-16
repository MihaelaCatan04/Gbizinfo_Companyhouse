package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.model.dto.CompanyDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

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
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void upsert(CompanyDto entity, String syncId) {
        CompanyDto current = companyMapper.findCompanyByCorporateNumber(entity.getCorporateNumber());

        if (current == null || hasChanged(current, entity)) {
            companyMapper.upsertCompany(entity);
        }
    }

    private boolean hasChanged(CompanyDto current, CompanyDto incoming) {
        return !Objects.equals(current.getName(), incoming.getName()) || !Objects.equals(current.getKana(), incoming.getKana()) || !Objects.equals(current.getNameEn(), incoming.getNameEn()) || !Objects.equals(current.getPostalCode(), incoming.getPostalCode()) || !Objects.equals(current.getLocation(), incoming.getLocation()) || !Objects.equals(current.getProcess(), incoming.getProcess()) || !Objects.equals(current.getAggregatedYear(), incoming.getAggregatedYear()) || !Objects.equals(current.getStatus(), incoming.getStatus()) || !Objects.equals(current.getCloseDate(), incoming.getCloseDate()) || !Objects.equals(current.getCloseCause(), incoming.getCloseCause()) || !Objects.equals(current.getKind(), incoming.getKind()) || !Objects.equals(current.getRepresentativeName(), incoming.getRepresentativeName()) || !Objects.equals(current.getCapitalStock(), incoming.getCapitalStock()) || !Objects.equals(current.getEmployeeNumber(), incoming.getEmployeeNumber()) || !Objects.equals(current.getCompanySizeMale(), incoming.getCompanySizeMale()) || !Objects.equals(current.getCompanySizeFemale(), incoming.getCompanySizeFemale()) || !Objects.equals(current.getBusinessSummary(), incoming.getBusinessSummary()) || !Objects.equals(current.getCompanyUrl(), incoming.getCompanyUrl()) || !Objects.equals(current.getFoundingYear(), incoming.getFoundingYear()) || !Objects.equals(current.getDateOfEstablishment(), incoming.getDateOfEstablishment()) || !Objects.equals(current.getQualificationGrade(), incoming.getQualificationGrade());
    }

    @Override
    protected String getCorporateNumber(CompanyDto entity) {
        return entity.getCorporateNumber();
    }
}