package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.service.PatentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patents")
@Validated
@Tag(name = "Patent", description = "Patent Entity Endpoint")
public class PatentController extends AbstractBatchController<PatentDto> {

    public PatentController(PatentService service) {
        super(service);
    }
}