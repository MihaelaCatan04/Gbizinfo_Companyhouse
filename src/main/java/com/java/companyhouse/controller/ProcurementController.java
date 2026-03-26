package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.service.ProcurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/procurements")
@RequiredArgsConstructor
public class ProcurementController {
    private final ProcurementService procurementService;

    @PostMapping
    public void receive(@RequestBody List<ProcurementDto> list) {
        procurementService.receive(list);
    }
}