package com.java.companyhouse.service;

import com.java.companyhouse.mapper.PatentMapper;
import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatentService extends AbstractBatchService<PatentDto> {

    private final PatentMapper patentMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<PatentDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<PatentDto> patents = snapshot.getEntities();

        for (PatentDto dto : patents) {
            patentMapper.upsertPatent(dto);
            patentMapper.upsertCompanyPatent(dto);
        }

        if (patents.isEmpty()) {
            patentMapper.softDeleteAllCompanyPatents(corporateNumber);
            return;
        }

        List<String> mergeKeys = patents.stream().map(PatentDto::getMergeKey).toList();

        patentMapper.softDeleteMissingCompanyPatents(corporateNumber, mergeKeys);
    }
}