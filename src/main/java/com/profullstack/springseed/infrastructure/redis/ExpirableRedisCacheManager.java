package com.profullstack.springseed.infrastructure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;

/**
 * This class is aim to add expire time support
 */
@Slf4j
public class ExpirableRedisCacheManager extends RedisCacheManager {


	public ExpirableRedisCacheManager(RedisOperations redisOperations) {
		super(redisOperations);
	}

	@SuppressWarnings("rawtypes")
	public ExpirableRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
		super(redisOperations, cacheNames);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ExpirableRedisCache createCache(String cacheName) {
		long expiration = computeExpiration(cacheName);
		return new ExpirableRedisCache(cacheName, (super.isUsePrefix() ? super.getCachePrefix().prefix(cacheName) : null), super.getRedisOperations(), expiration);
	}

	public ExpirableCache getExpirableCache(String name){
		return (ExpirableCache)super.getCache(name);
	}

	@Override
	protected Cache decorateCache(Cache cache) {

		if (isCacheAlreadyDecorated(cache)) {
			return cache;
		}

		return (isTransactionAware() ? new ExpirableTransactionAwareCacheDecorator(cache) : cache);
	}

	public void clearRegisteredCaches() {
		log.info("### clear all the cache ###");
		for(String name: super.getCacheNames()){
			Cache cache = getCache(name);
			if (cache != null) {
				cache.clear();
			}
		}
	}
}
