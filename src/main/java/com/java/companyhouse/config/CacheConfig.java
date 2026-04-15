package com.java.companyhouse.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Long> sharedIdL1Cache(@Value("${cache.l1.max-size}") int maxSize) {
        return Caffeine.newBuilder().maximumSize(maxSize).recordStats().build();
    }
}