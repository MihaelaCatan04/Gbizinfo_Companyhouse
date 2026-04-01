package com.java.companyhouse.service;

import com.java.companyhouse.mapper.CommendationMapper;
import com.java.companyhouse.model.dto.CommendationDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommendationService extends AbstractBatchService<CommendationDto> {

    private final CommendationMapper commendationMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<CommendationDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<CommendationDto> commendations = snapshot.getEntities();

        for (CommendationDto dto : commendations) {
            commendationMapper.upsertCommendation(dto);
            commendationMapper.upsertCompanyCommendation(dto);
        }

        if (commendations.isEmpty()) {
            commendationMapper.softDeleteAllCompanyCommendations(corporateNumber);
            return;
        }

        List<String> mergeKeys = commendations.stream().map(CommendationDto::getMergeKey).toList();

        commendationMapper.softDeleteMissingCompanyCommendations(corporateNumber, mergeKeys);
    }
}