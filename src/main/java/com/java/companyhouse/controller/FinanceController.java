package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.FinanceDto;
import com.java.companyhouse.service.FinanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finances")
@Validated
@Tag(name = "Finance", description = "Finance Entity Endpoint")
public class FinanceController extends AbstractBatchController<FinanceDto> {

    public FinanceController(FinanceService service) {
        super(service);
    }
}