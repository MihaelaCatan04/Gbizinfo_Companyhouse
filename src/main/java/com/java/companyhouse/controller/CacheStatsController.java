package com.java.companyhouse.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/cache")
public class CacheStatsController {

    private final Cache<String, Long> sharedIdL1Cache;

    public CacheStatsController(Cache<String, Long> sharedIdL1Cache) {
        this.sharedIdL1Cache = sharedIdL1Cache;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        CacheStats stats = sharedIdL1Cache.stats();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("hitCount", stats.hitCount());
        result.put("missCount", stats.missCount());
        result.put("hitRate", String.format("%.2f%%", stats.hitRate() * 100));
        result.put("missRate", String.format("%.2f%%", stats.missRate() * 100));
        result.put("loadCount", stats.loadCount());
        result.put("totalLoadTime_ms", stats.totalLoadTime() / 1_000_000);
        result.put("averageLoadPenalty_ms", stats.averageLoadPenalty() / 1_000_000);
        result.put("evictionCount", stats.evictionCount());
        result.put("currentSize", sharedIdL1Cache.estimatedSize());

        return result;
    }
}