package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.service.CertificationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certifications")
public class CertificationController extends AbstractBatchController<CertificationDto> {

    public CertificationController(CertificationService service) {
        super(service);
    }
}