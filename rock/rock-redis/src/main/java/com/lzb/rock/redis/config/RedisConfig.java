package com.lzb.rock.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置文件类
 * 
 * @author lzb https://blog.csdn.net/cadn_jueying/article/details/80736557
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Bean
	@Primary
	public RedisTemplate<String, String> stringSerializerRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(stringSerializer);
//		redisTemplate.setHashKeySerializer(stringSerializer);
//		redisTemplate.setHashValueSerializer(stringSerializer);
		return redisTemplate;
	}
}
