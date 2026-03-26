package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.mapper.RunMapper;
import com.java.companyhouse.model.dto.ClassificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final MergeMapper mergeMapper;
    private final RunMapper runMapper;

    @Transactional
    public void receive(List<ClassificationDto> list) {
        for (ClassificationDto dto : list) {
            mergeMapper.upsertClassification(dto);
            mergeMapper.upsertPatentClassification(dto);
            runMapper.insertRunClassification(
                    dto.getRunId(), dto.getMergeKey(), dto.getPatentMergeKey());
        }
    }
}