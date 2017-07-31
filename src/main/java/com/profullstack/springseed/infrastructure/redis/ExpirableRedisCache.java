package com.profullstack.springseed.infrastructure.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.springframework.util.Assert.hasText;


public class ExpirableRedisCache extends RedisCache implements ExpirableCache {

	private final RedisOperations redisOperations;
	private final ExpirableRedisCacheMetadata cacheMetadata;

	public ExpirableRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration) {
		super(name, prefix, redisOperations, expiration);

		this.cacheMetadata = new ExpirableRedisCacheMetadata(name, prefix);
		this.cacheMetadata.setDefaultExpiration(expiration);

		this.redisOperations = redisOperations;
	}

	@Override
	public void put(final Object key, final Object value, long expiration) {
		put(new RedisCacheElement(new RedisCacheKey(key).usePrefix(cacheMetadata.getKeyPrefix()).withKeySerializer(
			redisOperations.getKeySerializer()), value).expireAfter(expiration));
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, final Object value, long expiration) {

		return putIfAbsent(new RedisCacheElement(new RedisCacheKey(key).usePrefix(cacheMetadata.getKeyPrefix())
			.withKeySerializer(redisOperations.getKeySerializer()), value)
			.expireAfter(expiration));
	}

	/**
	 * Metadata required to maintain {@link RedisCache}. Keeps track of additional data structures required for processing
	 * cache operations.
	 *
	 * @author Christoph Strobl
	 * @since 1.5
	 */
	static class ExpirableRedisCacheMetadata {

		private final String cacheName;
		private final byte[] keyPrefix;
		private final byte[] setOfKnownKeys;
		private final byte[] cacheLockName;
		private long defaultExpiration = 0;

		/**
		 * @param cacheName must not be {@literal null} or empty.
		 * @param keyPrefix can be {@literal null}.
		 */
		public ExpirableRedisCacheMetadata(String cacheName, byte[] keyPrefix) {

			hasText(cacheName, "CacheName must not be null or empty!");
			this.cacheName = cacheName;
			this.keyPrefix = keyPrefix;

			StringRedisSerializer stringSerializer = new StringRedisSerializer();

			// name of the set holding the keys
			this.setOfKnownKeys = usesKeyPrefix() ? new byte[] {} : stringSerializer.serialize(cacheName + "~keys");
			this.cacheLockName = stringSerializer.serialize(cacheName + "~lock");
		}

		/**
		 * @return true if the {@link RedisCache} uses a prefix for key ranges.
		 */
		public boolean usesKeyPrefix() {
			return (keyPrefix != null && keyPrefix.length > 0);
		}

		/**
		 * Get the binary representation of the key prefix.
		 *
		 * @return never {@literal null}.
		 */
		public byte[] getKeyPrefix() {
			return this.keyPrefix;
		}

		/**
		 * Get the binary representation of the key identifying the data structure used to maintain known keys.
		 *
		 * @return never {@literal null}.
		 */
		public byte[] getSetOfKnownKeysKey() {
			return setOfKnownKeys;
		}

		/**
		 * Get the binary representation of the key identifying the data structure used to lock the cache.
		 *
		 * @return never {@literal null}.
		 */
		public byte[] getCacheLockKey() {
			return cacheLockName;
		}

		/**
		 * Get the name of the cache.
		 *
		 * @return
		 */
		public String getCacheName() {
			return cacheName;
		}

		/**
		 * Set the default expiration time in seconds
		 *
		 */
		public void setDefaultExpiration(long seconds) {
			this.defaultExpiration = seconds;
		}

		/**
		 * Get the default expiration time in seconds.
		 *
		 * @return
		 */
		public long getDefaultExpiration() {
			return defaultExpiration;
		}

	}
}
