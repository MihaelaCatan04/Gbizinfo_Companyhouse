package com.java.companyhouse.service;

import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.model.dto.CompanyDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService extends AbstractBatchService<CompanyDto> {

    private final CompanyMapper companyMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<CompanyDto> snapshot) {
        snapshot.getEntities().forEach(companyMapper::upsertCompany);
    }
}