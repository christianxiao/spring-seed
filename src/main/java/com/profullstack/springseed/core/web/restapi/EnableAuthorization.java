package com.profullstack.springseed.core.web.restapi;

import java.lang.annotation.*;

/**
 * Created by christianxiao on 7/30/17.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableAuthorization {
	boolean value() default true;
}
