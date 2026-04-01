package com.java.companyhouse.controller;

import com.java.companyhouse.model.receiver.MajorShareholderBatchRequest;
import com.java.companyhouse.service.MajorShareholderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/major-shareholders")
@RequiredArgsConstructor
public class MajorShareholderController {

    private final MajorShareholderService majorShareholderService;

    @PostMapping
    public void receive(@RequestBody MajorShareholderBatchRequest request) {
        majorShareholderService.receive(request);
    }
}