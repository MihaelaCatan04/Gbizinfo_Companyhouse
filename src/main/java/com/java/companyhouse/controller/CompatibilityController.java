package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import com.java.companyhouse.service.CompatibilityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compatibilities")
public class CompatibilityController extends AbstractBatchController<CompatibilityOfChildcareAndWorkDto> {

    public CompatibilityController(CompatibilityService service) {
        super(service);
    }
}