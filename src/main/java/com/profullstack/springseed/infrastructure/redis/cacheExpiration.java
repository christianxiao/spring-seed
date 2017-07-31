package com.profullstack.springseed.infrastructure.redis;

import java.lang.annotation.*;

/**
 * Created by christianxiao on 7/28/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface cacheExpiration {
	String value();
	long expiration() default 0;
}
