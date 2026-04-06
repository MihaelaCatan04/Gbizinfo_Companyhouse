package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ClassificationMapper;
import com.java.companyhouse.model.dto.ClassificationDto;
import com.java.companyhouse.model.receiver.ClassificationBatchRequest;
import com.java.companyhouse.model.receiver.PatentClassificationSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final ClassificationMapper classificationMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final TransactionTemplate transactionTemplate;

    public void receive(ClassificationBatchRequest request) {
        List<PatentClassificationSnapshot> patents = request == null || request.getPatents() == null ? Collections.emptyList() : request.getPatents();

        for (PatentClassificationSnapshot snapshot : patents) {
            transactionTemplate.executeWithoutResult(status -> processSnapshot(snapshot));
        }
    }

    private void processSnapshot(PatentClassificationSnapshot snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        String patentMergeKey = snapshot.getPatentMergeKey();
        List<ClassificationDto> rawClassifications = safeClassifications(snapshot);

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (rawClassifications.isEmpty()) {
            deleteAllClassifications(corporateNumber, patentMergeKey);
            return;
        }

        String syncId = UUID.randomUUID().toString();
        List<ClassificationDto> classifications = prepareClassifications(rawClassifications);

        upsertClassifications(classifications, patentMergeKey, corporateNumber, syncId);
        cleanupMissingClassifications(corporateNumber, patentMergeKey, syncId);
    }

    private boolean isValidSnapshot(PatentClassificationSnapshot snapshot) {
        return snapshot != null && snapshot.getPatentMergeKey() != null && snapshot.getCorporateNumber() != null;
    }

    private List<ClassificationDto> safeClassifications(PatentClassificationSnapshot snapshot) {
        return snapshot.getClassifications() == null ? Collections.emptyList() : snapshot.getClassifications();
    }

    private void deleteAllClassifications(String corporateNumber, String patentMergeKey) {
        classificationMapper.softDeleteAllPatentClassifications(corporateNumber, patentMergeKey);
    }

    private List<ClassificationDto> prepareClassifications(List<ClassificationDto> raw) {
        List<ClassificationDto> classifications = new ArrayList<>(raw);
        classifications.removeIf(dto -> dto == null || dto.getMergeKey() == null);
        classifications.sort(Comparator.comparing(ClassificationDto::getMergeKey));
        return classifications;
    }

    private void upsertClassifications(List<ClassificationDto> classifications, String patentMergeKey, String corporateNumber, String syncId) {
        for (ClassificationDto dto : classifications) {
            classificationMapper.upsertClassification(dto);
            classificationMapper.upsertPatentClassification(dto.getMergeKey(), patentMergeKey, corporateNumber, syncId);
        }
    }

    private void cleanupMissingClassifications(String corporateNumber, String patentMergeKey, String syncId) {
        classificationMapper.softDeleteMissingPatentClassifications(corporateNumber, patentMergeKey, syncId);
    }
}
