package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CompanyDto;
import com.java.companyhouse.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public void receive(@RequestBody List<CompanyDto> companies) {
        companyService.receive(companies);
    }
}