package com.profullstack.springseed.infrastructure.web.restapi;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by christianxiao on 7/31/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableJwtConfig {
	boolean value() default false;
	String secretPropertyName() default "jwt.secret";
	long expiration() default -1;
}
