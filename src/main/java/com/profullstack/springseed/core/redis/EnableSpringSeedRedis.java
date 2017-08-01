package com.profullstack.springseed.core.redis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by christianxiao on 7/26/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SpringSeedRedis.class)
public @interface EnableSpringSeedRedis {

	String propertyPrefix() default "jedisConnectionFactory";

	long defaultExpiration() default 0;

	/*
	 * Expire time in seconds.
	 */
	cacheExpiration[] cacheExpirations() default {};

	boolean clearBeforeStart() default false;
}
