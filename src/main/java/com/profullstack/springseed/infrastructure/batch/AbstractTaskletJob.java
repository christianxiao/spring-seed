package com.profullstack.springseed.infrastructure.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by christianxiao on 7/22/17.
 */
@Component
@Slf4j
public abstract class AbstractTaskletJob implements FactoryBean<Job>, Tasklet {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Override
    public Job getObject() throws Exception {
        Step step = stepBuilderFactory.get(this.getClass().getName() + ":step")
                .tasklet(this.getClass().newInstance())
                .build();
        return jobBuilderFactory.get(this.getClass().getName()).start(step).build();
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public abstract RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception;
}
