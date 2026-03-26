package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certifications")
@RequiredArgsConstructor
public class CertificationController {
    private final CertificationService certificationService;

    @PostMapping
    public void receive(@RequestBody List<CertificationDto> list) {
        certificationService.receive(list);
    }
}