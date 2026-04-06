package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.MajorShareholderMapper;
import com.java.companyhouse.model.dto.MajorShareholderDto;
import com.java.companyhouse.model.receiver.FinanceMajorShareholderSnapshot;
import com.java.companyhouse.model.receiver.MajorShareholderBatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MajorShareholderService {

    private final MajorShareholderMapper majorShareholderMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final TransactionTemplate transactionTemplate;

    public void receive(MajorShareholderBatchRequest request) {
        List<FinanceMajorShareholderSnapshot> finances = request == null || request.getFinances() == null ? Collections.emptyList() : request.getFinances();

        for (FinanceMajorShareholderSnapshot snapshot : finances) {
            transactionTemplate.executeWithoutResult(status -> processSnapshot(snapshot));
        }
    }

    private void processSnapshot(FinanceMajorShareholderSnapshot snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        String financeMergeKey = snapshot.getFinanceMergeKey();
        List<MajorShareholderDto> rawShareholders = safeShareholders(snapshot);

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (rawShareholders.isEmpty()) {
            deleteAllShareholders(corporateNumber, financeMergeKey);
            return;
        }

        String syncId = UUID.randomUUID().toString();
        List<MajorShareholderDto> shareholders = prepareShareholders(rawShareholders);

        upsertShareholders(shareholders, financeMergeKey, corporateNumber, syncId);
        cleanupMissingShareholders(corporateNumber, financeMergeKey, syncId);
    }

    private boolean isValidSnapshot(FinanceMajorShareholderSnapshot snapshot) {
        return snapshot != null && snapshot.getFinanceMergeKey() != null && snapshot.getCorporateNumber() != null;
    }

    private List<MajorShareholderDto> safeShareholders(FinanceMajorShareholderSnapshot snapshot) {
        return snapshot.getShareholders() == null ? Collections.emptyList() : snapshot.getShareholders();
    }

    private void deleteAllShareholders(String corporateNumber, String financeMergeKey) {
        majorShareholderMapper.softDeleteAllFinanceShareholders(corporateNumber, financeMergeKey);
    }

    private List<MajorShareholderDto> prepareShareholders(List<MajorShareholderDto> rawShareholders) {
        List<MajorShareholderDto> shareholders = new ArrayList<>(rawShareholders);
        shareholders.removeIf(dto -> dto == null || dto.getMergeKey() == null);
        shareholders.sort(Comparator.comparing(MajorShareholderDto::getMergeKey));
        return shareholders;
    }

    private void upsertShareholders(List<MajorShareholderDto> shareholders, String financeMergeKey, String corporateNumber, String syncId) {
        for (MajorShareholderDto dto : shareholders) {
            majorShareholderMapper.upsertMajorShareholder(dto);
            majorShareholderMapper.upsertFinanceShareholder(dto.getMergeKey(), financeMergeKey, corporateNumber, syncId);
        }
    }

    private void cleanupMissingShareholders(String corporateNumber, String financeMergeKey, String syncId) {
        majorShareholderMapper.softDeleteMissingFinanceShareholders(corporateNumber, financeMergeKey, syncId);
    }
}
