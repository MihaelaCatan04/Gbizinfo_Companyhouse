package com.java.companyhouse.controller;

import com.java.companyhouse.model.receiver.NestedBatchRequest;
import com.java.companyhouse.model.receiver.NestedSnapshot;
import com.java.companyhouse.service.AbstractNestedBatchService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class AbstractNestedBatchController<S extends NestedSnapshot<T>, T> {

    private final AbstractNestedBatchService<S, T> service;

    protected AbstractNestedBatchController(AbstractNestedBatchService<S, T> service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Batch processed successfully")
    @ApiResponse(responseCode = "400", description = "Malformed JSON or missing required fields")
    @ApiResponse(responseCode = "500", description = "Unexpected internal error")
    public void receive(@RequestBody @Valid NestedBatchRequest<S> request) {
        service.receive(request);
    }
}