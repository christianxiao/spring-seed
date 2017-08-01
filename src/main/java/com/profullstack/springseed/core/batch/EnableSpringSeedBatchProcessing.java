package com.profullstack.springseed.core.batch;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by christianxiao on 7/22/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SpringSeedBatchConfig.class)
public @interface EnableSpringSeedBatchProcessing {
}
