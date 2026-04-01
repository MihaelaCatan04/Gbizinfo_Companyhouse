package com.java.companyhouse.controller;

import com.java.companyhouse.model.receiver.TopicBatchRequest;
import com.java.companyhouse.service.AbstractBatchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class AbstractBatchController<T> {

    private final AbstractBatchService<T> service;

    protected AbstractBatchController(AbstractBatchService<T> service) {
        this.service = service;
    }

    @PostMapping
    public void receive(@RequestBody TopicBatchRequest<T> request) {
        service.receive(request);
    }
}