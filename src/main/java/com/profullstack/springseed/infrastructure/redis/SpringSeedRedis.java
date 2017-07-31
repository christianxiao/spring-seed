package com.profullstack.springseed.infrastructure.redis;

import com.profullstack.springseed.infrastructure.utils.BeanPropertyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


@Slf4j
@Configuration
@EnableCaching
public class SpringSeedRedis implements CachingConfigurer, ImportAware {

	public static final String SPRING_REDIS_SENTINEL_MASTER = "spring.redis.sentinel.master";
	public static final String SPRING_REDIS_SENTINEL_NODES = "spring.redis.sentinel.nodes";

	public static final String REDIS_SENTINEL_MASTER = "redis.sentinel.master";
	public static final String REDIS_SENTINEL_NODES = "redis.sentinel.nodes";

	@Autowired
	private Environment environment;

	private long defaultExpiration;
	private String propertyPrefix;
	private Map<String, Long> expiresMap = new TreeMap<>();
	private boolean clearBeforeStart;

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableSpringSeedRedis.class.getName()));
		this.defaultExpiration =  attributes.getNumber("defaultExpiration").longValue();
		this.propertyPrefix = attributes.getString("propertyPrefix");
		AnnotationAttributes[] annotationAttributes = attributes.getAnnotationArray("cacheExpirations");
		for(AnnotationAttributes a: annotationAttributes){
			expiresMap.put(a.getString("value"), a.getNumber("expiration").longValue());
		}
		this.clearBeforeStart = attributes.getBoolean("clearBeforeStart");
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory;

		Properties properties = new Properties();
		if(environment.containsProperty(REDIS_SENTINEL_MASTER)){
			properties.setProperty(SPRING_REDIS_SENTINEL_MASTER, environment.getProperty(REDIS_SENTINEL_MASTER));
			properties.setProperty(SPRING_REDIS_SENTINEL_NODES, environment.getProperty(REDIS_SENTINEL_NODES));

			PropertySource propertySource = new PropertiesPropertySource(RedisSentinelConfiguration.class.getName(), properties);
			RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(propertySource);
			jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig);
		}else{
			jedisConnectionFactory = new JedisConnectionFactory();
		}
		BeanPropertyUtil.setBeanProperty(jedisConnectionFactory, (AbstractEnvironment)environment, this.propertyPrefix);
		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	public ExpirableRedisCacheManager cacheManager() {
		ExpirableRedisCacheManager redisCacheManager = new ExpirableRedisCacheManager(redisTemplate());
		redisCacheManager.setUsePrefix(true);
		redisCacheManager.setDefaultExpiration(defaultExpiration);
		//redisCacheManager.setCacheNames(getAllCacheNames());
		redisCacheManager.setExpires(expiresMap);

		if(this.clearBeforeStart){
			redisCacheManager.clearRegisteredCaches();
		}
		return redisCacheManager;
	}

	@Override
	public CacheResolver cacheResolver() {
		return null;
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return null;
	}

	/**
	 * this was used to generate unique key for each method, the key will be like:
	 * className + methodName + args.
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(":");
				sb.append(method.getName());
				sb.append(":");
				for (Object obj : params) {
					sb.append(String.valueOf(obj));
					sb.append(":");
				}
				return sb.toString();
			}
		};
	}
}
