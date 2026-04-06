package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ClassificationDto;
import com.java.companyhouse.model.receiver.PatentClassificationSnapshot;
import com.java.companyhouse.service.ClassificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classifications")
@Validated
@Tag(name = "Classification", description = "Classification Entity Endpoint")
public class ClassificationController extends AbstractNestedBatchController<PatentClassificationSnapshot, ClassificationDto> {

    public ClassificationController(ClassificationService service) {
        super(service);
    }
}