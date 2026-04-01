package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.service.ProcurementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/procurements")
public class ProcurementController extends AbstractBatchController<ProcurementDto> {

    public ProcurementController(ProcurementService service) {
        super(service);
    }
}