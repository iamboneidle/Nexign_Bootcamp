package brt.brt_service.Configs;

import brt.brt_service.Redis.DAO.Models.MsisdnToMinutesLeft;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Конфигурационный класс для базы данных Redis.
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig {
    /**
     * Хост Redis.
     */
    @Value("${spring.data.redis.host}")
    private String redisHost;
    /**
     * Порт Redis.
     */
    @Value("${spring.data.redis.port}")
    private int redisPort;

    /**
     * Создает и настраивает шаблон RedisTemplate для работы с кешем Redis.
     *
     * @param redisConnectionFactory фабрика подключения к Redis
     * @return настроенный RedisTemplate
     */
    @Bean
    public RedisTemplate<String, MsisdnToMinutesLeft> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, MsisdnToMinutesLeft> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * Создает операции для работы с множествами в Redis.
     *
     * @param redisTemplate шаблон RedisTemplate
     * @return операции для работы с множествами
     */
    @Bean
    public SetOperations<String, MsisdnToMinutesLeft> setOperations(RedisTemplate<String, MsisdnToMinutesLeft> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * Создает менеджер кеша для управления кешем Redis.
     *
     * @param factory фабрика подключения к Redis
     * @return менеджер кеша Redis
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration)
                .build();
    }

    /**
     * Создает фабрику подключения к Redis с указанным хостом и портом.
     *
     * @return фабрика подключения к Redis
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }
}