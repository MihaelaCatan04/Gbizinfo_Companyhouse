package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.SubsidyDto;
import com.java.companyhouse.service.SubsidyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subsidies")
@RequiredArgsConstructor
public class SubsidyController {
    private final SubsidyService subsidyService;

    @PostMapping
    public void receive(@RequestBody List<SubsidyDto> list) {
        subsidyService.receive(list);
    }
}