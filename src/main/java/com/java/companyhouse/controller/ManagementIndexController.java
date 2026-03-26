package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import com.java.companyhouse.service.ManagementIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/management-indexes")
@RequiredArgsConstructor
public class ManagementIndexController {
    private final ManagementIndexService managementIndexService;

    @PostMapping
    public void receive(@RequestBody List<ManagementIndexDto> list) {
        managementIndexService.receive(list);
    }
}