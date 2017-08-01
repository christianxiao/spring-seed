package com.profullstack.springseed.core.redis;

import org.springframework.cache.Cache;

/**
 * Created by christianxiao on 7/29/17.
 */
public interface ExpirableCache extends Cache {
	void put(final Object key, final Object value, long expiration);
	ValueWrapper putIfAbsent(Object key, final Object value, long expiration);
}
