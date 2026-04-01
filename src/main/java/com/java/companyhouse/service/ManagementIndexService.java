package com.java.companyhouse.service;

import com.java.companyhouse.mapper.ManagementIndexMapper;
import com.java.companyhouse.model.dto.ManagementIndexDto;
import com.java.companyhouse.model.receiver.FinanceManagementIndexSnapshot;
import com.java.companyhouse.model.receiver.ManagementIndexBatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementIndexService {

    private final ManagementIndexMapper managementIndexMapper;

    @Transactional
    public void receive(ManagementIndexBatchRequest request) {
        List<FinanceManagementIndexSnapshot> finances = request == null || request.getFinances() == null ? Collections.emptyList() : request.getFinances();

        for (FinanceManagementIndexSnapshot snapshot : finances) {
            processFinance(snapshot);
        }
    }

    private void processFinance(FinanceManagementIndexSnapshot snapshot) {
        if (snapshot == null || snapshot.getFinanceMergeKey() == null) {
            return;
        }

        String financeMergeKey = snapshot.getFinanceMergeKey();
        List<ManagementIndexDto> managementIndexes = snapshot.getManagementIndexes() == null ? Collections.emptyList() : snapshot.getManagementIndexes();

        for (ManagementIndexDto dto : managementIndexes) {
            if (dto == null) {
                continue;
            }
            managementIndexMapper.upsertManagementIndex(dto);
            managementIndexMapper.upsertFinanceManagement(dto);
        }

        if (managementIndexes.isEmpty()) {
            managementIndexMapper.softDeleteAllFinanceManagementIndexes(financeMergeKey);
            return;
        }

        List<String> mergeKeys = managementIndexes.stream().filter(dto -> dto != null && dto.getMergeKey() != null).map(ManagementIndexDto::getMergeKey).toList();

        if (mergeKeys.isEmpty()) {
            managementIndexMapper.softDeleteAllFinanceManagementIndexes(financeMergeKey);
            return;
        }

        managementIndexMapper.softDeleteMissingFinanceManagementIndexes(financeMergeKey, mergeKeys);
    }
}