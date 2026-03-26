package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ClassificationDto;
import com.java.companyhouse.service.ClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/classifications")
@RequiredArgsConstructor
public class ClassificationController {
    private final ClassificationService classificationService;

    @PostMapping
    public void receive(@RequestBody List<ClassificationDto> list) {
        classificationService.receive(list);
    }
}