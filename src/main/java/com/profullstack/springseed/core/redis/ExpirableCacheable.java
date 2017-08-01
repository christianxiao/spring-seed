package com.profullstack.springseed.core.redis;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExpirableCacheable {

	/**
	 * the cache name.
	 */
	String cacheName();

	/**
	 * the cache key. Will override default keyName by generator.
	 */
	String key() default "";

	/**
	 * -1: use default, in seconds.
	 */
	long expiration() default -1;
}
