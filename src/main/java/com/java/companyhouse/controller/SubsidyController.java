package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.SubsidyDto;
import com.java.companyhouse.service.SubsidyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subsidies")
public class SubsidyController extends AbstractBatchController<SubsidyDto> {

    public SubsidyController(SubsidyService service) {
        super(service);
    }
}