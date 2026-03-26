package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import com.java.companyhouse.service.WorkplaceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workplace-infos")
@RequiredArgsConstructor
public class WorkplaceInfoController {
    private final WorkplaceInfoService workplaceInfoService;

    @PostMapping
    public void receive(@RequestBody List<WorkplaceInfoDto> list) {
        workplaceInfoService.receive(list);
    }
}