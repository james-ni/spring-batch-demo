package com.james.demo;

import com.james.demo.persistence.Product;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchJobConfiguration {
    private final PlatformTransactionManager transactionManager;

    @Bean
    public ItemReader<Product> itemWriter() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new ClassPathResource("sample-data.dat"))
                .fixedLength()
                .columns(
                        new Range(1, 10),
                        new Range(11, 11)
                )
                .names("name", "category")
                .targetType(Product.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Product> jdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .sql("INSERT INTO product (id, name, category) VALUES (:id, :name, :category)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public JpaItemWriter<Product> jpaItemWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Product>().entityManagerFactory(entityManagerFactory).build();
    }

    @Bean
    public Job importProductJob(JobRepository jobRepository, Step importProductStep) {
        return new JobBuilder("importProductJob", jobRepository)
                .start(importProductStep)
                .build();
    }

    @Bean
    public Step importProductStep(
            JobRepository jobRepository,
            ItemReader<Product> itemReader,
//            JdbcBatchItemWriter<Product> itemWriter,
            JpaItemWriter<Product> itemWriter,
            ItemProcessor<Product, Product> itemProcessor
    ) {
        return new StepBuilder("importProductStep", jobRepository)
                .<Product, Product>chunk(2, transactionManager)
                .faultTolerant()
                .skip(DataAccessException.class)
                .skipLimit(5)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
}
