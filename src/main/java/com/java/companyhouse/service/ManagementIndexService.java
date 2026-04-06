package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ManagementIndexMapper;
import com.java.companyhouse.model.dto.ManagementIndexDto;
import com.java.companyhouse.model.receiver.FinanceManagementIndexSnapshot;
import com.java.companyhouse.model.receiver.ManagementIndexBatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ManagementIndexService {

    private final ManagementIndexMapper managementIndexMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final TransactionTemplate transactionTemplate;

    public void receive(ManagementIndexBatchRequest request) {
        List<FinanceManagementIndexSnapshot> finances = request == null || request.getFinances() == null ? Collections.emptyList() : request.getFinances();

        for (FinanceManagementIndexSnapshot snapshot : finances) {
            transactionTemplate.executeWithoutResult(status -> processSnapshot(snapshot));
        }
    }

    private void processSnapshot(FinanceManagementIndexSnapshot snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        String financeMergeKey = snapshot.getFinanceMergeKey();
        List<ManagementIndexDto> rawManagementIndexes = safeManagementIndexes(snapshot);

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (rawManagementIndexes.isEmpty()) {
            deleteAllManagementIndexes(corporateNumber, financeMergeKey);
            return;
        }

        String syncId = UUID.randomUUID().toString();
        List<ManagementIndexDto> managementIndexes = prepareManagementIndexes(rawManagementIndexes);

        upsertManagementIndexes(managementIndexes, financeMergeKey, corporateNumber, syncId);
        cleanupMissingManagementIndexes(corporateNumber, financeMergeKey, syncId);
    }

    private boolean isValidSnapshot(FinanceManagementIndexSnapshot snapshot) {
        return snapshot != null && snapshot.getFinanceMergeKey() != null && snapshot.getCorporateNumber() != null;
    }

    private List<ManagementIndexDto> safeManagementIndexes(FinanceManagementIndexSnapshot snapshot) {
        return snapshot.getManagementIndexes() == null ? Collections.emptyList() : snapshot.getManagementIndexes();
    }

    private void deleteAllManagementIndexes(String corporateNumber, String financeMergeKey) {
        managementIndexMapper.softDeleteAllFinanceManagementIndexes(corporateNumber, financeMergeKey);
    }

    private List<ManagementIndexDto> prepareManagementIndexes(List<ManagementIndexDto> rawManagementIndexes) {
        List<ManagementIndexDto> managementIndexes = new ArrayList<>(rawManagementIndexes);
        managementIndexes.removeIf(dto -> dto == null || dto.getMergeKey() == null);
        managementIndexes.sort(Comparator.comparing(ManagementIndexDto::getMergeKey));
        return managementIndexes;
    }

    private void upsertManagementIndexes(List<ManagementIndexDto> managementIndexes, String financeMergeKey, String corporateNumber, String syncId) {
        for (ManagementIndexDto dto : managementIndexes) {
            managementIndexMapper.upsertManagementIndex(dto);
            managementIndexMapper.upsertFinanceManagement(dto.getMergeKey(), financeMergeKey, corporateNumber, syncId);
        }
    }

    private void cleanupMissingManagementIndexes(String corporateNumber, String financeMergeKey, String syncId) {
        managementIndexMapper.softDeleteMissingFinanceManagementIndexes(corporateNumber, financeMergeKey, syncId);
    }
}
