package com.profullstack.springseed.core.batch;

import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by christianxiao on 7/22/17.
 */
@Configuration
public class SpringSeedBatchConfig {

    @Bean
    public JobBuilderFactory jobBuilderFactory() throws Exception {
        return new JobBuilderFactory(jobRepository().getObject());
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory() throws Exception {
        return new StepBuilderFactory(jobRepository().getObject(), batchTransactionManager());
    }

    @Bean
    public MapJobRepositoryFactoryBean jobRepository() throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean =  new MapJobRepositoryFactoryBean();
        mapJobRepositoryFactoryBean.setTransactionManager(batchTransactionManager());
        mapJobRepositoryFactoryBean.afterPropertiesSet();
        return mapJobRepositoryFactoryBean;
    }

/*    @Bean
    public JobRepository jobRepository() throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean =  new MapJobRepositoryFactoryBean();
        mapJobRepositoryFactoryBean.setTransactionManager(batchTransactionManager());
        mapJobRepositoryFactoryBean.afterPropertiesSet();
        return mapJobRepositoryFactoryBean.getObject();
    }*/

    @Bean
    public PlatformTransactionManager batchTransactionManager() throws Exception {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobLauncher getJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository().getObject());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public JobExplorer jobExplorer() throws Exception {
        MapJobExplorerFactoryBean mapJobExplorerFactoryBean = new MapJobExplorerFactoryBean();
        mapJobExplorerFactoryBean.setRepositoryFactory(jobRepository());
        mapJobExplorerFactoryBean.afterPropertiesSet();
        return mapJobExplorerFactoryBean.getObject();
    }

    @Bean
    public JobLocator jobLocator() {
        return new MapJobRegistry();
    }
}
