package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import com.java.companyhouse.service.CompatibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/compatibilities")
@RequiredArgsConstructor
public class CompatibilityController {
    private final CompatibilityService compatibilityService;

    @PostMapping
    public void receive(@RequestBody List<CompatibilityOfChildcareAndWorkDto> list) {
        compatibilityService.receive(list);
    }
}