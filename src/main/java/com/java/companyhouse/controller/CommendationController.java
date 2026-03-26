package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CommendationDto;
import com.java.companyhouse.service.CommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/commendations")
@RequiredArgsConstructor
public class CommendationController {
    private final CommendationService commendationService;

    @PostMapping
    public void receive(@RequestBody List<CommendationDto> list) {
        commendationService.receive(list);
    }
}