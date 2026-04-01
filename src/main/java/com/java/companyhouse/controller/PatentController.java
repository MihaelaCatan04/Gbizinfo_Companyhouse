package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.service.PatentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patents")
public class PatentController extends AbstractBatchController<PatentDto> {

    public PatentController(PatentService service) {
        super(service);
    }
}