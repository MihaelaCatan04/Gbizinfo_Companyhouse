package com.java.companyhouse.service;

import com.java.companyhouse.mapper.ClassificationMapper;
import com.java.companyhouse.model.dto.ClassificationDto;
import com.java.companyhouse.model.receiver.ClassificationBatchRequest;
import com.java.companyhouse.model.receiver.PatentClassificationSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final ClassificationMapper classificationMapper;

    @Transactional
    public void receive(ClassificationBatchRequest request) {
        List<PatentClassificationSnapshot> patents =
                request == null || request.getPatents() == null
                        ? Collections.emptyList()
                        : request.getPatents();

        for (PatentClassificationSnapshot snapshot : patents) {
            processPatent(snapshot);
        }
    }

    private void processPatent(PatentClassificationSnapshot snapshot) {
        if (snapshot == null || snapshot.getPatentMergeKey() == null) {
            return;
        }

        String patentMergeKey = snapshot.getPatentMergeKey();
        List<ClassificationDto> classifications =
                snapshot.getClassifications() == null
                        ? Collections.emptyList()
                        : snapshot.getClassifications();

        for (ClassificationDto dto : classifications) {
            if (dto == null) {
                continue;
            }
            classificationMapper.upsertClassification(dto);
            classificationMapper.upsertPatentClassification(dto);
        }

        if (classifications.isEmpty()) {
            classificationMapper.softDeleteAllPatentClassifications(patentMergeKey);
            return;
        }

        List<String> mergeKeys = classifications.stream()
                .map(ClassificationDto::getMergeKey)
                .toList();

        classificationMapper.softDeleteMissingPatentClassifications(patentMergeKey, mergeKeys);
    }
}