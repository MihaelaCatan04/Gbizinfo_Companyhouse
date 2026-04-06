package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.service.ProcurementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/procurements")
@Validated
@Tag(name = "Procurement", description = "Procurement Entity Endpoint")
public class ProcurementController extends AbstractBatchController<ProcurementDto> {

    public ProcurementController(ProcurementService service) {
        super(service);
    }
}