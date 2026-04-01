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
import java.util.UUID;

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

        if (shareholders.isEmpty()) {
            majorShareholderMapper.softDeleteAllFinanceShareholders(financeMergeKey);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        for (MajorShareholderDto dto : shareholders) {
            if (dto == null || dto.getMergeKey() == null) {
                continue;
            }

            majorShareholderMapper.upsertMajorShareholder(dto);
            majorShareholderMapper.upsertFinanceShareholder(dto.getMergeKey(), financeMergeKey, syncId);
        }

        majorShareholderMapper.softDeleteMissingFinanceShareholders(financeMergeKey, syncId);
    }
}