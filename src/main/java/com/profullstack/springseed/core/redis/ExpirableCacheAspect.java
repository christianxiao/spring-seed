package com.profullstack.springseed.core.redis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Gavin on 11/30/16.
 */
@Component
@Aspect
@Slf4j
public class ExpirableCacheAspect {

	private final SpelExpressionParser parser = new SpelExpressionParser();

	private final Map<String, SpelExpression> localKeyCache = new ConcurrentHashMap<String, SpelExpression>(64);

	@Autowired
	private KeyGenerator keyGenerator;

	@Autowired
	private ExpirableRedisCacheManager cacheManager;

	enum CacheType {
		GET,
		PUT,
		EVICT
	}

	@Around("@annotation(config)")
	public Object singleCacheable(ProceedingJoinPoint pjp, ExpirableCacheable config) throws Throwable {
		final String cacheName = config.cacheName();
		final String keyStr = config.key();
		final long expiration = config.expiration();
		return mainCacheProcess(pjp, cacheName, keyStr, expiration, CacheType.GET);
	}

	@Around("@annotation(config)")
	public Object singleCachePut(ProceedingJoinPoint pjp, ExpirableCachePut config) throws Throwable {
		final String cacheName = config.cacheName();
		final String keyStr = config.key();
		final long expiration = config.expiration();
		return mainCacheProcess(pjp, cacheName, keyStr, expiration, CacheType.PUT);
	}

	@Around("@annotation(config)")
	public Object singleCacheEvict(ProceedingJoinPoint pjp, ExpirableCacheEvict config) throws Throwable {
		final String cacheName = config.cacheName();
		final String keyStr = config.key();
		return mainCacheProcess(pjp, cacheName, keyStr, 0, CacheType.EVICT);
	}

	private Object mainCacheProcess(ProceedingJoinPoint pjp, String cacheName, String keyStr, long expiration,  CacheType cacheType) throws Throwable {
		ExpirableCache cache;
		Object key;
		try {
			cache = getCache(cacheName);
			key = generateKey(keyStr, pjp);
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("### failed to get cache : {} or generate key : {}, will invoke the target service.", cacheName, keyStr);
			return pjp.proceed();
		}
		if (cacheType == CacheType.GET) {
			try {
				Cache.ValueWrapper valueWrapper = cache.get(key);
				if (valueWrapper != null && valueWrapper.get() != null) {
					return valueWrapper.get();
				}
			} catch (Throwable throwable) {
				// just print the exception, will invoke the target service.
				throwable.printStackTrace();
				log.warn("### failed to get value from cache for key : {}, will invoke the target service.", keyStr);
			}
		}
		// invoke the target service, will not catch the exception if any exception happened here.
		Object result = pjp.proceed();
		try {
			if (cacheType == CacheType.EVICT) {
				cache.evict(key);
			} else {
				if(expiration >= 0) {
					cache.put(key, result, expiration);
				}else{
					cache.put(key, result);
				}
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			log.warn("### failed to evict or update the cache for key :", key);
		}
		return result;
	}

	private ExpirableCache getCache(String cacheName) throws ExpirableCacheException {
		ExpirableCache cache = cacheManager.getExpirableCache(cacheName);
		if (cache == null) {
			throw new ExpirableCacheException("### cache name:" +
				cacheName + " does not exist");
		}
		return cache;
	}

	/**
	 * generate the key based on SPel expression.
	 */
	protected Object generateKey(String key, ProceedingJoinPoint pjp) throws ExpirableCacheException {
		try {
			Object target = pjp.getTarget();
			Method method = ((MethodSignature) pjp.getSignature()).getMethod();
			Object[] allArgs = pjp.getArgs();
			if (StringUtils.hasText(key)) {
				CacheExpressionDataObject cacheExpressionDataObject = new CacheExpressionDataObject(method, allArgs, target, target.getClass());
				EvaluationContext evaluationContext = new StandardEvaluationContext(cacheExpressionDataObject);
				SpelExpression spelExpression = getExpression(key, method);
				spelExpression.setEvaluationContext(evaluationContext);
				return spelExpression.getValue();
			}
			return keyGenerator.generate(target, method, allArgs);
		} catch (Throwable t) {
			throw new ExpirableCacheException("### generate key failed");
		}
	}

	private SpelExpression getExpression(String expression, Method method) {
		String key = toString(method, expression);
		SpelExpression rtn = localKeyCache.get(key);
		if (rtn == null) {
			rtn = (SpelExpression) this.parser.parseExpression(expression);
			localKeyCache.put(key, rtn);
		}
		return rtn;
	}

	private String toString(Method method, String expression) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getDeclaringClass().getName());
		sb.append("#");
		sb.append(method.toString());
		sb.append("#");
		sb.append(expression);
		return sb.toString();
	}
}
