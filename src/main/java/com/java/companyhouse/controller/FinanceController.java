package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.FinanceDto;
import com.java.companyhouse.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/finances")
@RequiredArgsConstructor
public class FinanceController {
    private final FinanceService financeService;

    @PostMapping
    public void receive(@RequestBody List<FinanceDto> list) {
        financeService.receive(list);
    }
}