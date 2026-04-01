package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.FinanceDto;
import com.java.companyhouse.service.FinanceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finances")
public class FinanceController extends AbstractBatchController<FinanceDto> {

    public FinanceController(FinanceService service) {
        super(service);
    }
}