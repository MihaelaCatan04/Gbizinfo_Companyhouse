package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CompanyDto;
import com.java.companyhouse.service.CompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController extends AbstractBatchController<CompanyDto> {

    public CompanyController(CompanyService service) {
        super(service);
    }
}