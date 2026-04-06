package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import com.java.companyhouse.service.WorkplaceInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workplaceinfos")
@Validated
@Tag(name = "WorkplaceInfo", description = "WorkplaceInfo Entity Endpoint")
public class WorkplaceInfoController extends AbstractBatchController<WorkplaceInfoDto> {

    public WorkplaceInfoController(WorkplaceInfoService service) {
        super(service);
    }
}