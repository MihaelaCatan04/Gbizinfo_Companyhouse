package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompatibilityService {

    private final MergeMapper mergeMapper;

    @Transactional
    public void receive(List<CompatibilityOfChildcareAndWorkDto> list) {
        list.forEach(mergeMapper::upsertCompatibility);
    }
}