package com.onlinebookreader.configuration;

import java.time.Duration;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig implements CachingConfigurer {
	
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		
		return RedisCacheConfiguration.defaultCacheConfig()
				//cache expire after 10 minutes
				.entryTtl(Duration.ofMinutes(10))
				//
				.disableCachingNullValues()
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		
		
	}

}
