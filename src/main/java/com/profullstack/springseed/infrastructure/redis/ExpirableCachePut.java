package com.profullstack.springseed.infrastructure.redis;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExpirableCachePut {

	/**
	 * the cache name.
	 */
	String cacheName();

	/**
	 * the cache key.
	 */
	String key() default "";

	/**
	 * -1: use default, in seconds.
	 */
	long expiration() default -1;
}
