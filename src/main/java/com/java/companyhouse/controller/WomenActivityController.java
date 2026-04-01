package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import com.java.companyhouse.service.WomenActivityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/women-activities")
public class WomenActivityController extends AbstractBatchController<WomenActivityInfoDto> {

    public WomenActivityController(WomenActivityService service) {
        super(service);
    }
}