package com.profullstack.springseed.sample.batch.config;

import com.profullstack.springseed.core.batch.EnableSpringSeedBatchProcessing;
import com.profullstack.springseed.sample.batch.components.Batchs;
import com.profullstack.springseed.sample.domain.config.DomainContextConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by christianxiao on 7/20/17.
 */
@Configuration
//@EnableBatchProcessing
@EnableSpringSeedBatchProcessing
@Import(value={DomainContextConfig.class})
@ComponentScan(basePackageClasses = {Batchs.class})
public class BatchContextConfig {
}
