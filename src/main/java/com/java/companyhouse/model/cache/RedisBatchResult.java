package com.java.companyhouse.model.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class RedisBatchResult {
    private Map<String, Long> l1BatchFromRedis;
    private List<String> dbMisses;
}