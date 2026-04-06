package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.service.CertificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certifications")
@Validated
@Tag(name = "Certification", description = "Certification Entity Endpoint")
public class CertificationController extends AbstractBatchController<CertificationDto> {

    public CertificationController(CertificationService service) {
        super(service);
    }
}