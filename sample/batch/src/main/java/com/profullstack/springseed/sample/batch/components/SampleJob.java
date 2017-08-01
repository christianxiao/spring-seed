package com.profullstack.springseed.sample.batch.components;

import com.profullstack.springseed.core.batch.AbstractTaskletJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * Created by christianxiao on 7/22/17.
 */
@Component
@Slf4j
public class SampleJob extends AbstractTaskletJob {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();

        for(int i = 0;i<10;i++) {
            log.info(jobParameters.getParameters().toString());
        }
        return RepeatStatus.FINISHED;
    }
}
