package com.profullstack.springseed.infrastructure.jpa;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by christianxiao on 7/26/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SpringSeedJpa.class)
public @interface EnableSpringSeedJpa {

	String propertyPrefix() default "basicDataSource";

	/**
	 * Useful when you need mutiple dataSources
	 * @return
     */
	String beanNamePrefix() default "";
	Class<?>[] baseEntityClasses();

}
