package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.CommendationDto;
import com.java.companyhouse.service.CommendationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commendations")
@Validated
@Tag(name = "Commendation", description = "Commendation Entity Endpoint")
public class CommendationController extends AbstractBatchController<CommendationDto> {

    public CommendationController(CommendationService service) {
        super(service);
    }
}