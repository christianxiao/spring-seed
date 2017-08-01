package com.profullstack.springseed.core.web.restapi;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by christianxiao on 7/23/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SpringSeedRestApiMvcConfigration.class)
public @interface EnableSpringSeedRestApiMvc {
	boolean disableCors() default false;
	boolean parseAuthorizationHeader() default false;
	EnableJwtConfig enableJwtConfig() default @EnableJwtConfig();
	boolean enableSwagger2() default false;
}
