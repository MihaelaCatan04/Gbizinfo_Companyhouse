package com.java.companyhouse.service;

import com.java.companyhouse.mapper.MergeMapper;
import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WomenActivityService {

    private final MergeMapper mergeMapper;

    @Transactional
    public void receive(List<WomenActivityInfoDto> list) {
        list.forEach(mergeMapper::upsertWomenActivity);
    }
}