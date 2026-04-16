package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import com.java.companyhouse.model.receiver.FinanceManagementIndexSnapshot;
import com.java.companyhouse.service.ManagementIndexService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/managementindexes")
@Validated
@Tag(name = "ManagementIndex", description = "ManagementIndex Entity Endpoint")
public class ManagementIndexController extends AbstractNestedBatchController<FinanceManagementIndexSnapshot, ManagementIndexDto> {

    public ManagementIndexController(ManagementIndexService service) {
        super(service);
    }
}