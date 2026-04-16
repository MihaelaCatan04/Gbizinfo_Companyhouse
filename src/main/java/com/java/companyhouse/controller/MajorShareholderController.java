package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.MajorShareholderDto;
import com.java.companyhouse.model.receiver.FinanceMajorShareholderSnapshot;
import com.java.companyhouse.service.MajorShareholderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/majorshareholders")
@Validated
@Tag(name = "MajorShareholder", description = "MajorShareholder Entity Endpoint")
public class MajorShareholderController extends AbstractNestedBatchController<FinanceMajorShareholderSnapshot, MajorShareholderDto> {

    public MajorShareholderController(MajorShareholderService service) {
        super(service);
    }
}
