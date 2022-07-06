package com.nft.common.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static RedisUtils redisUtils;

	@PostConstruct
	public void init() {
		redisUtils = this;
		redisUtils.redisTemplate = this.redisTemplate;
	}

	public static String opsForValueGet(String key) {
		String value = redisUtils.redisTemplate.opsForValue().get(key);
		return value;
	}

	public static void opsForValueSet(String key, String value) {
		redisUtils.redisTemplate.opsForValue().set(key, value);
	}

}
