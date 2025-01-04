package com.james.demo;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class SpringBatchDemoApplicationTests {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Test
    void testNewJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder().addLocalDateTime("date", LocalDateTime.now()).toJobParameters();
        JobExecution execution = jobLauncher.run(job, jobParameters);
    }

    @Test
    void testRestartJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("bucket", "guangtoutou-s3-event-notification")
                .addString("key", "sample-data.dat")
                .toJobParameters();
        JobExecution execution = jobLauncher.run(job, jobParameters);
    }

}
