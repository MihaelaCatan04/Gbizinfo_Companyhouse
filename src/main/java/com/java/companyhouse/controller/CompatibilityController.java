package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import com.java.companyhouse.service.CompatibilityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compatibilities")
@Validated
@Tag(name = "Compatibility", description = "Compatibility Of Childcare and Work Entity Endpoint")
public class CompatibilityController extends AbstractBatchController<CompatibilityOfChildcareAndWorkDto> {

    public CompatibilityController(CompatibilityService service) {
        super(service);
    }
}