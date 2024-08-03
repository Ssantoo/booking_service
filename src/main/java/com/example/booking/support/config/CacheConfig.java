package com.example.booking.support.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@EnableCaching
//@Configuration
//@RequiredArgsConstructor
//public class CacheConfig extends CachingConfigurerSupport {
//
//    @Bean(name="CacheManager")
//    @Override
//    public CacheManager cacheManager() {
//        return new CacheManager();
//    }
//
//    @Bean(name="RedisCacheManager")
//    @Override
//    public CacheManager cacheManager() {
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
//                        new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
//                        new GenericJackson2JsonRedisSerializer(objectMapper)))
//                .disableCachingNullValues()
//                .computePrefixWith(CacheKeyPrefix.prefixed(CACHE_NAME_PREFIX))
//                .prefixCacheNameWith(CACHE_NAME_PREFIX)
//                .entryTtl(TTL_1_HOUR);
//
//        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
//                        redisConnectionFactory)
//                .withInitialCacheConfigurations(cacheConfigurationMap)
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }
//}
