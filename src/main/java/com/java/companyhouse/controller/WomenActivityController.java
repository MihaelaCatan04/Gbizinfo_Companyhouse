package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import com.java.companyhouse.service.WomenActivityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/womenactivities")
@Validated
@Tag(name = "WomenActivity", description = "WomenActivity Entity Endpoint")
public class WomenActivityController extends AbstractBatchController<WomenActivityInfoDto> {

    public WomenActivityController(WomenActivityService service) {
        super(service);
    }
}