package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.service.PatentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patents")
@RequiredArgsConstructor
public class PatentController {
    private final PatentService patentService;

    @PostMapping
    public void receive(@RequestBody List<PatentDto> list) {
        patentService.receive(list);
    }
}