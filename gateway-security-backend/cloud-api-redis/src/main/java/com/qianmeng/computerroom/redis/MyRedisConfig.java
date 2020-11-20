package com.qianmeng.computerroom.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郭超
 * Date:2020-09-17 11:03
 * Description: 自定义 Redis配置类,实例化一个RedisTepplate对象,
 * 泛型采用<String,Object>键值对,避免了默认设置<Object,Object>的频繁强转操作
 */
@Slf4j
@Configuration
@EnableCaching
public class MyRedisConfig {

    /**
     * 缓存生存时间
     */
    private Duration timeToLive = Duration.ofDays(1);

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //redis缓存配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(this.timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues();
        //缓存配置map
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        //自定义缓存名，后面使用的@Cacheable的CacheName
        cacheConfigurationMap.put("users", config);
        cacheConfigurationMap.put("default", config);
        //根据redis缓存配置和reid连接工厂生成redis缓存管理器
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();
        log.info("自定义RedisCacheManager加载完成");
        return redisCacheManager;
    }

    /**
     * redisTemplate模板提供给其他类对redis数据库进行操作
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());
        log.info("自定义RedisTemplate加载完成");
        return redisTemplate;
    }

    /**
     * redis键序列化使用StrngRedisSerializer
     *
     * @return
     */
    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    /**
     * redis值序列化使用json序列化器
     *
     * @return
     */
    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * 缓存键自动生成器
     *
     * @return
     */
    /*@Bean
    public KeyGenerator myKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName()).append(":");
            sb.append(method.getName()).append(":");
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }*/

}
