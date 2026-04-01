package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CommendationDto;
import com.java.companyhouse.service.CommendationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commendations")
public class CommendationController extends AbstractBatchController<CommendationDto> {

    public CommendationController(CommendationService service) {
        super(service);
    }
}