package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.BaseInfoDto;
import com.java.companyhouse.service.BaseInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baseinfos")
@Validated
@Tag(name = "BaseInfo", description = "BaseInfo Entity Endpoint")
public class BaseInfoController extends AbstractBatchController<BaseInfoDto> {

    public BaseInfoController(BaseInfoService service) {
        super(service);
    }
}