package com.profullstack.springseed.infrastructure.redis;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by christianxiao on 3/22/17.
 * A simple distributed lock implementation based on Redis
 * ref: https://redis.io/topics/distlock
 */

public class SimpleRedisDlmLock {

	private static final String LOCK_KEY_BASE = SimpleRedisDlmLock.class.getCanonicalName();
	private static final String NX = "NX";
	private static final String PX = "PX";
	private static final String SCRIPT_CHECK_THEN_DELETE =
		"if redis.call(\"get\",KEYS[1]) == ARGV[1] then" + "\n" +
		"    return redis.call(\"del\",KEYS[1])" + "\n" +
		"else" + "\n" +
		"    return 0" + "\n" +
		"end";

	private RedisTemplate redisTemplate;
	private String lockKey;
	private Long maxLockTime; //milliseconds

	private String lockValue;

	private static final int RETRY_COUNT = 3;
	private static final long RETRY_SLEEP_MILLS = 300;

	public SimpleRedisDlmLock(RedisTemplate redisTemplate, String keyName, Long maxLockTime){
		this.redisTemplate = redisTemplate;
		this.lockKey = LOCK_KEY_BASE + ":" + keyName;
		this.maxLockTime = maxLockTime;
	}

	public Boolean tryLock(){
		String uuid = UUID.randomUUID().toString();
		Boolean success = (Boolean) redisTemplate.execute((RedisConnection connection) -> {
			Jedis jedis = (Jedis)connection.getNativeConnection();
			String res = jedis.set(lockKey, uuid, NX, PX, maxLockTime);
			if (res != null && res.equals("OK")) {
				return true;
			}else{
				return false;
			}
		});
		if(success){
			this.lockValue = uuid;
			return true;
		}else{
			return false;
		}
	}

	public Boolean releaseLock(){
		Boolean success = (Boolean) redisTemplate.execute((RedisConnection connection) -> {
			Jedis jedis = (Jedis)connection.getNativeConnection();
			Long res = (Long)jedis.eval(SCRIPT_CHECK_THEN_DELETE, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
			if (res != null && res.equals(1L)) {
				return true;
			}else{
				return false;
			}
		});
		if(success){
			return true;
		}else{
			return false;
		}
	}

	public Boolean tryLockMultiTimes() {
		for (int i = 0; i < RETRY_COUNT; i++) {
			if (tryLock()) {
				return true;
			}
			try {
				Thread.sleep(RETRY_SLEEP_MILLS);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		return false;
	}

	public Boolean tryReleaseLockMultiTimes() {
		for (int i = 0; i < RETRY_COUNT; i++) {
			if (releaseLock()) {
				return true;
			}
			try {
				Thread.sleep(RETRY_SLEEP_MILLS);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		return false;
	}

}
