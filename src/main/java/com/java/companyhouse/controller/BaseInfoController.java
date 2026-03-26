package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.BaseInfoDto;
import com.java.companyhouse.service.BaseInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/base-infos")
@RequiredArgsConstructor
public class BaseInfoController {
    private final BaseInfoService baseInfoService;

    @PostMapping
    public void receive(@RequestBody List<BaseInfoDto> list) {
        baseInfoService.receive(list);
    }
}