package org.example;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheStore<T> {

    private final Cache<String, T> cache;
    private final int DEFAULT_MINUTES_TIMEOUT = 15;

    public CacheStore() {
        cache = CacheBuilder.newBuilder()
            .expireAfterWrite(DEFAULT_MINUTES_TIMEOUT, TimeUnit.MINUTES)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();
    }

    public List<T> getAll() {
        return cache.getAllPresent(cache.asMap().keySet()).values().asList();
    }

    public void add(T value) {
        if (value != null) {
            cache.put(String.valueOf(value.hashCode()), value);
        }
    }
}
