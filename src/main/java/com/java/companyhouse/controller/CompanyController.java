package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CompanyDto;
import com.java.companyhouse.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
@Validated
@Tag(name = "Company", description = "Company Entity Endpoint")
public class CompanyController extends AbstractBatchController<CompanyDto> {

    public CompanyController(CompanyService service) {
        super(service);
    }
}