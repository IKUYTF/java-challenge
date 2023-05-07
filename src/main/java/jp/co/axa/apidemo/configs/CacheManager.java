package jp.co.axa.apidemo.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * Cache Manager
 * Set the max size and expire time for cache
 */
public class CacheManager {

    @Bean
    public CaffeineCacheManager cacheManager() {
        Caffeine<Object, Object> caffeineCacheBuilder =
                Caffeine.newBuilder()
                        .maximumSize(200)
                        .expireAfterAccess(1, TimeUnit.MINUTES);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager("employees");
        cacheManager.setCaffeine(caffeineCacheBuilder);
        return cacheManager;
    }
}
