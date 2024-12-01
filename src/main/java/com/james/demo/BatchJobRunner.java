package com.james.demo;

import com.james.demo.persistence.Product;
import com.james.demo.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchJobRunner implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final JobLauncher jobLauncher;
    private final Job job;

    @Override
    public void run(String... args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder().addLocalDateTime("date", LocalDateTime.now()).toJobParameters();
        JobExecution execution = jobLauncher.run(job, jobParameters);

        log.info( "Job execution finished with status: {}" , execution.getStepExecutions());

    }
}
