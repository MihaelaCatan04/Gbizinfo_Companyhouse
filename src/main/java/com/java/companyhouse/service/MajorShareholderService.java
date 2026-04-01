package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MajorShareholderMapper;
import com.java.companyhouse.model.dto.MajorShareholderDto;
import com.java.companyhouse.model.receiver.FinanceMajorShareholderSnapshot;
import com.java.companyhouse.model.receiver.MajorShareholderBatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorShareholderService {

    private final MajorShareholderMapper majorShareholderMapper;

    @Transactional
    public void receive(MajorShareholderBatchRequest request) {
        List<FinanceMajorShareholderSnapshot> finances = request == null || request.getFinances() == null ? Collections.emptyList() : request.getFinances();

        for (FinanceMajorShareholderSnapshot snapshot : finances) {
            processFinance(snapshot);
        }
    }

    private void processFinance(FinanceMajorShareholderSnapshot snapshot) {
        if (snapshot == null || snapshot.getFinanceMergeKey() == null) {
            return;
        }

        String financeMergeKey = snapshot.getFinanceMergeKey();
        List<MajorShareholderDto> shareholders = snapshot.getShareholders() == null ? Collections.emptyList() : snapshot.getShareholders();

        for (MajorShareholderDto dto : shareholders) {
            if (dto == null) {
                continue;
            }
            majorShareholderMapper.upsertMajorShareholder(dto);
            majorShareholderMapper.upsertFinanceShareholder(dto);
        }

        if (shareholders.isEmpty()) {
            majorShareholderMapper.softDeleteAllFinanceShareholders(financeMergeKey);
            return;
        }

        List<String> mergeKeys = shareholders.stream().filter(dto -> dto != null && dto.getMergeKey() != null).map(MajorShareholderDto::getMergeKey).toList();

        if (mergeKeys.isEmpty()) {
            majorShareholderMapper.softDeleteAllFinanceShareholders(financeMergeKey);
            return;
        }

        majorShareholderMapper.softDeleteMissingFinanceShareholders(financeMergeKey, mergeKeys);
    }
}