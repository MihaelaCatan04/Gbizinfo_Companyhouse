package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import com.java.companyhouse.service.WorkplaceInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workplace-infos")
public class WorkplaceInfoController extends AbstractBatchController<WorkplaceInfoDto> {

    public WorkplaceInfoController(WorkplaceInfoService service) {
        super(service);
    }
}