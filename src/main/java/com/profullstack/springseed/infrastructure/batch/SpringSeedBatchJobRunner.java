package com.profullstack.springseed.infrastructure.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.batch.core.launch.support.JvmSystemExiter;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.batch.core.launch.support.SystemExiter;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by christianxiao on 7/23/17.
 * Usage: java -cp "batch/*" com.profullstack.jseed.infrastructure.configuration.batch.SpringSeedBatchJobRunner com.profullstack.springseed.jproject.batch.configuration.BatchContextConfig sampleJob aaa=bb
 */
public class SpringSeedBatchJobRunner {
    protected static final Log logger = LogFactory.getLog(SpringSeedBatchJobRunner.class);

    private ExitCodeMapper exitCodeMapper = new SimpleJvmExitCodeMapper();

    private static SystemExiter systemExiter = new JvmSystemExiter();

    private JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();

    @Autowired
    private JobLauncher launcher;

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobRepository jobRepository;

    /**
     * Delegate to the exiter to (possibly) exit the VM gracefully.
     *
     * @param status
     */
    public void exit(int status) {
        systemExiter.exit(status);
    }

    int start(String jobPath, String jobIdentifier, String[] parameters, Set<String> opts) {

        SpringBatchApplicationContext context = null;

        try {
            //Add initializer support here!
            context = SpringBatchApplicationContext.instance(jobPath);
            //end
/*            try {
                context = new AnnotationConfigApplicationContext(Class.forName(jobPath));
            } catch (ClassNotFoundException cnfe) {
                context = new ClassPathXmlApplicationContext(jobPath);
            }*/

            context.getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);

            Assert.state(launcher != null, "A JobLauncher must be provided.  Please add one to the configuration.");
            if (opts.contains("-restart") || opts.contains("-next")) {
                Assert.state(jobExplorer != null,
                        "A JobExplorer must be provided for a restart or start next operation.  Please add one to the configuration.");
            }

            String jobName = jobIdentifier;

            JobParameters jobParameters = jobParametersConverter.getJobParameters(StringUtils
                    .splitArrayElementsIntoProperties(parameters, "="));
            Assert.isTrue(parameters == null || parameters.length == 0 || !jobParameters.isEmpty(),
                    "Invalid JobParameters " + Arrays.asList(parameters)
                            + ". If parameters are provided they should be in the form name=value (no whitespace).");

            Job job = null;
            if (jobLocator != null) {
                try {
                    job = jobLocator.getJob(jobName);
                } catch (NoSuchJobException e) {
                }
            }
            if (job == null) {
                job = (Job) context.getBean(jobName);
            }

            JobExecution jobExecution = launcher.run(job, jobParameters);
            return exitCodeMapper.intValue(jobExecution.getExitStatus().getExitCode());

        }
        catch (Throwable e) {
            String message = "Job Terminated in error: " + e.getMessage();
            logger.error(message, e);
            return exitCodeMapper.intValue(ExitStatus.FAILED.getExitCode());
        }
        finally {
            if (context != null) {
                context.close();
            }
        }
    }


    public static void main(String[] args){
        SpringSeedBatchJobRunner command = new SpringSeedBatchJobRunner();

        List<String> newargs = new ArrayList<String>(Arrays.asList(args));

        try {
            if (System.in.available() > 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line = " ";
                while (line != null) {
                    if (!line.startsWith("#") && StringUtils.hasText(line)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Stdin arg: " + line);
                        }
                        newargs.add(line);
                    }
                    line = reader.readLine();
                }
            }
        }
        catch (IOException e) {
            logger.warn("Could not access stdin (maybe a platform limitation)");
            if (logger.isDebugEnabled()) {
                logger.debug("Exception details", e);
            }
        }

        Set<String> opts = new HashSet<String>();
        List<String> params = new ArrayList<String>();

        int count = 0;
        String jobPath = null;
        String jobIdentifier = null;

        for (String arg : newargs) {
            switch (count) {
                case 0:
                    jobPath = arg;
                    break;
                case 1:
                    jobIdentifier = arg;
                    break;
                default:
                    params.add(arg);
                    break;
            }
            count++;
        }

        if (jobPath == null || jobIdentifier == null) {
            String message = "At least 2 arguments are required: JobPath/JobClass and jobIdentifier.";
            logger.error(message);
            command.exit(1);
            return;
        }

        String[] parameters = params.toArray(new String[params.size()]);

        int result = command.start(jobPath, jobIdentifier, parameters, opts);
        command.exit(result);
    }
}
