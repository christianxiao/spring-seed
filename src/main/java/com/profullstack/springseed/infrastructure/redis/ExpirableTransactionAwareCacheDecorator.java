package com.profullstack.springseed.infrastructure.redis;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Created by christianxiao on 7/29/17.
 */
public class ExpirableTransactionAwareCacheDecorator extends TransactionAwareCacheDecorator implements ExpirableCache {

	private final ExpirableRedisCache targetCache;


	/**
	 * Create a new TransactionAwareCache for the given target Cache.
	 *
	 * @param targetCache the target Cache to decorate
	 */
	public ExpirableTransactionAwareCacheDecorator(ExpirableRedisCache targetCache) {
		super(targetCache);
		this.targetCache = targetCache;
	}

	public ExpirableTransactionAwareCacheDecorator(Cache targetCache) {
		super(targetCache);
		this.targetCache = (ExpirableRedisCache)targetCache;
	}

	@Override
	public void put(final Object key, final Object value, final long expiration) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					targetCache.put(key, value, expiration);
				}
			});
		}
		else {
			this.targetCache.put(key, value, expiration);
		}
	}

	@Override
	public ValueWrapper putIfAbsent(final Object key, final Object value, final long expiration) {
		return this.targetCache.putIfAbsent(key, value, expiration);
	}
}
