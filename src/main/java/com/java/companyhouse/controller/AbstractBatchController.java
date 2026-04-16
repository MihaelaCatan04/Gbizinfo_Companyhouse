package com.java.companyhouse.controller;

import com.java.companyhouse.model.receiver.TopicBatchRequest;
import com.java.companyhouse.service.AbstractBatchService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class AbstractBatchController<T> {

    private final AbstractBatchService<T> service;

    protected AbstractBatchController(AbstractBatchService<T> service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Batch processed successfully")
    @ApiResponse(responseCode = "400", description = "Malformed JSON or missing required fields")
    @ApiResponse(responseCode = "500", description = "Unexpected internal error")
    public void receive(@RequestBody @Valid TopicBatchRequest<T> request) {
        service.receive(request);
    }
}