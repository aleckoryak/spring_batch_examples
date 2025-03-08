package com.linkedin.batch;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class FromJdbcPagingToStringJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    ItemWriter<Order> stringItemWriter;

    @Autowired
    JdbcPagingItemReader<Order> jdbcPagingItemReader;

    @Bean
    public Step fromJdbcPagingToStringStep() throws Exception {
        return this.stepBuilderFactory.get("fromJdbcPagingToStringStep")
                .<Order, Order>chunk(10)
                .reader(jdbcPagingItemReader)
                .writer(stringItemWriter).build();
    }

    @Bean
    public Job jobFromJdbcPagingToString() throws Exception {
        return this.jobBuilderFactory.get("jobFromJdbcPagingToString").start(fromJdbcPagingToStringStep()).build();
    }

}
