package com.java.companyhouse.cache;

import com.java.companyhouse.model.cache.RedisBatchResult;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import com.java.companyhouse.service.cache.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Cache {

    private final RedisCacheService redis;
    private final com.github.benmanes.caffeine.cache.Cache<String, Long> l1;

    public Cache(RedisCacheService redis, com.github.benmanes.caffeine.cache.Cache<String, Long> l1) {
        this.redis = redis;
        this.l1 = l1;
    }

    public Map<String, Long> resolveAll(String prefix, List<String> keys, Function<List<String>, List<MergeKeyIdDto>> dbBatchLookup) {
        if (isEmpty(keys)) {
            return new HashMap<>();
        }

        Map<String, Long> result = new HashMap<>();
        List<String> l1Misses = collectL1HitsAndMisses(prefix, keys, result);

        if (l1Misses.isEmpty()) {
            return result;
        }

        List<String> dbMisses = resolveMissesFromRedis(prefix, l1Misses, result);
        if (dbMisses.isEmpty()) {
            return result;
        }

        Map<String, Long> dbBatch = loadDbBatchIntoResult(prefix, dbMisses, dbBatchLookup, result);
        putBatchToBothCaches(dbBatch);

        return result;
    }

    public void warmAll(String prefix, List<String> keys, Function<List<String>, List<MergeKeyIdDto>> dbBatchLookup) {
        if (isEmpty(keys)) {
            return;
        }

        List<String> l1Misses = findL1Misses(prefix, keys);
        if (l1Misses.isEmpty()) {
            return;
        }

        List<String> dbMisses = warmMissesFromRedis(prefix, l1Misses);
        if (dbMisses.isEmpty()) {
            return;
        }

        Map<String, Long> dbBatch = loadDbBatch(prefix, dbMisses, dbBatchLookup);
        putBatchToBothCaches(dbBatch);
    }

    private List<String> findL1Misses(String prefix, List<String> keys) {
        return keys.stream().filter(key -> l1.getIfPresent(fullKey(prefix, key)) == null).toList();
    }

    private List<String> collectL1HitsAndMisses(String prefix, List<String> keys, Map<String, Long> result) {
        List<String> l1Misses = new ArrayList<>();

        for (String key : keys) {
            String fullKey = fullKey(prefix, key);
            Long hit = l1.getIfPresent(fullKey);

            if (hit != null) {
                result.put(key, hit);
            } else {
                l1Misses.add(key);
            }
        }

        return l1Misses;
    }

    private List<String> resolveMissesFromRedis(String prefix, List<String> keys, Map<String, Long> result) {
        RedisBatchResult redisBatch = resolveFromRedis(prefix, keys, result);

        if (!redisBatch.getL1BatchFromRedis().isEmpty()) {
            l1.putAll(redisBatch.getL1BatchFromRedis());
        }

        return redisBatch.getDbMisses();
    }

    private List<String> warmMissesFromRedis(String prefix, List<String> keys) {
        RedisBatchResult redisBatch = warmFromRedis(prefix, keys);

        if (!redisBatch.getL1BatchFromRedis().isEmpty()) {
            l1.putAll(redisBatch.getL1BatchFromRedis());
        }

        return redisBatch.getDbMisses();
    }

    private RedisBatchResult resolveFromRedis(String prefix, List<String> keys, Map<String, Long> result) {
        List<String> fullKeys = toFullKeys(prefix, keys);
        List<String> redisValues = redis.multiGet(fullKeys);

        Map<String, Long> l1BatchFromRedis = new LinkedHashMap<>();
        List<String> dbMisses = new ArrayList<>();

        for (int i = 0; i < keys.size(); i++) {
            String value = redisValues.get(i);
            String key = keys.get(i);
            String fullKey = fullKeys.get(i);

            if (value != null) {
                long id = Long.parseLong(value);
                l1BatchFromRedis.put(fullKey, id);
                result.put(key, id);
            } else {
                dbMisses.add(key);
            }
        }

        return new RedisBatchResult(l1BatchFromRedis, dbMisses);
    }

    private RedisBatchResult warmFromRedis(String prefix, List<String> keys) {
        List<String> fullKeys = toFullKeys(prefix, keys);
        List<String> redisValues = redis.multiGet(fullKeys);

        Map<String, Long> l1BatchFromRedis = new LinkedHashMap<>();
        List<String> dbMisses = new ArrayList<>();

        for (int i = 0; i < keys.size(); i++) {
            String value = redisValues.get(i);
            String key = keys.get(i);
            String fullKey = fullKeys.get(i);

            if (value != null) {
                l1BatchFromRedis.put(fullKey, Long.parseLong(value));
            } else {
                dbMisses.add(key);
            }
        }

        return new RedisBatchResult(l1BatchFromRedis, dbMisses);
    }

    private Map<String, Long> loadDbBatch(String prefix, List<String> keys, Function<List<String>, List<MergeKeyIdDto>> dbBatchLookup) {
        return loadDbBatchIntoResult(prefix, keys, dbBatchLookup, null);
    }

    private Map<String, Long> loadDbBatchIntoResult(String prefix, List<String> keys, Function<List<String>, List<MergeKeyIdDto>> dbBatchLookup, Map<String, Long> result) {
        List<MergeKeyIdDto> fromDb = dbBatchLookup.apply(keys);
        Map<String, Long> cacheBatch = new LinkedHashMap<>();

        for (MergeKeyIdDto row : fromDb) {
            String key = row.getMergeKey();
            Long id = row.getId();
            cacheBatch.put(fullKey(prefix, key), id);

            if (result != null) {
                result.put(key, id);
            }
        }

        return cacheBatch;
    }

    private void putBatchToBothCaches(Map<String, Long> batch) {
        if (batch == null || batch.isEmpty()) {
            return;
        }

        l1.putAll(batch);
        redis.multiSet(batch);
    }

    private List<String> toFullKeys(String prefix, List<String> keys) {
        return keys.stream().map(key -> fullKey(prefix, key)).collect(Collectors.toList());
    }

    private String fullKey(String prefix, String key) {
        return prefix + key;
    }

    private boolean isEmpty(List<String> keys) {
        return keys == null || keys.isEmpty();
    }
}