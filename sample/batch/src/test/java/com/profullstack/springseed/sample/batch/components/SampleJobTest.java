package com.profullstack.springseed.sample.batch.components;

import com.profullstack.springseed.sample.batch.BatchAbstractTest;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * Created by christianxiao on 7/22/17.
 */
public class SampleJobTest extends BatchAbstractTest {

    @Resource(name="sampleJob")
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Test
    public void testExecute() throws Exception {
        jobLauncher.run(job, new JobParameters()).getExitStatus();
    }

}
