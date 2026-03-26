package com.java.companyhouse.controller;

import com.java.companyhouse.service.RunService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/run")
@RequiredArgsConstructor
public class RunController {

    private final RunService runService;

    @PostMapping("/{runId}/complete/{entity}")
    public void completeEntity(@PathVariable String runId,
                               @PathVariable String entity) {
        runService.completeEntity(runId, entity);
    }

    @PostMapping("/{runId}/complete")
    public void completeRun(@PathVariable String runId) {
        runService.completeRun(runId);
    }
}