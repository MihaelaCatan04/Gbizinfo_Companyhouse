package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.BaseInfoDto;
import com.java.companyhouse.service.BaseInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base-infos")
public class BaseInfoController extends AbstractBatchController<BaseInfoDto> {

    public BaseInfoController(BaseInfoService service) {
        super(service);
    }
}