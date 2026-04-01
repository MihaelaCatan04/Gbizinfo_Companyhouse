package com.java.companyhouse.controller;

import com.java.companyhouse.model.receiver.ClassificationBatchRequest;
import com.java.companyhouse.service.ClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classifications")
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    @PostMapping
    public void receive(@RequestBody ClassificationBatchRequest request) {
        classificationService.receive(request);
    }
}