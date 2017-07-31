package com.profullstack.springseed.jproject.batch.configuration;

import com.profullstack.springseed.infrastructure.batch.EnableSpringSeedBatchProcessing;
import com.profullstack.springseed.jproject.batch.components.Batchs;
import com.profullstack.springseed.jproject.domain.configuration.DomainContextConfig;
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
