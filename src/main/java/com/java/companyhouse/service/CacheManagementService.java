package com.java.companyhouse.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheManagementService {

    private final Cache<String, Long> sharedIdL1Cache;

    public Map<String, Object> getStats() {
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