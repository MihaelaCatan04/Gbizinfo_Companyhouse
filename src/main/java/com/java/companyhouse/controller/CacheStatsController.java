package com.java.companyhouse.controller;

import com.java.companyhouse.service.cache.CacheManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CacheStatsController {

    private final CacheManagementService cacheManagementService;

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return cacheManagementService.getStats();
    }
}