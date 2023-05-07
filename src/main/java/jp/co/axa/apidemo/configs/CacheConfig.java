package jp.co.axa.apidemo.configs;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Empty cache config class
 * Only use to enable cache
 * -> Because this is only a demo, do not add configs in this class
 */
@Configuration
@EnableCaching
public class CacheConfig {
}
