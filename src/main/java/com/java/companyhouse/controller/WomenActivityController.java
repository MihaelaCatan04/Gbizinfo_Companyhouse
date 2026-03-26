package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import com.java.companyhouse.service.WomenActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/women-activities")
@RequiredArgsConstructor
public class WomenActivityController {
    private final WomenActivityService womenActivityService;

    @PostMapping
    public void receive(@RequestBody List<WomenActivityInfoDto> list) {
        womenActivityService.receive(list);
    }
}