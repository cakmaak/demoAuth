package com.demoSec.demoauth.authorization.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Boolean> authorizationCache(
            MeterRegistry meterRegistry
    ) {

        Cache<String, Boolean> cache =
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(3))
                        .maximumSize(10_000)
                        .recordStats()
                        .build();

        CaffeineCacheMetrics.monitor(
                meterRegistry,
                cache,
                "authorizationCache"
        );

        return cache;
    }
}