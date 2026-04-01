package com.java.companyhouse.service;

import com.java.companyhouse.mapper.PatentMapper;
import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatentService extends AbstractBatchService<PatentDto> {

    private final PatentMapper patentMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<PatentDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<PatentDto> patents = snapshot.getEntities();

        if (patents.isEmpty()) {
            patentMapper.softDeleteAllCompanyPatents(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        for (PatentDto dto : patents) {
            patentMapper.upsertPatent(dto);
            patentMapper.upsertCompanyPatent(dto, syncId);
        }

        patentMapper.softDeleteMissingCompanyPatents(corporateNumber, syncId);
    }
}