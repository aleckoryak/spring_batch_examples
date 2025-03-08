package com.linkedin.batch;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class FromJdbcCursorToFileJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    ItemWriter<Order> stringItemWriter;

    @Autowired
    ItemWriter<Order> fileItemWriterForOrder;

    @Autowired
    ItemReader<Order> jdbcCursorReader;

    @Bean
    public Step chunkDBOneThreadToFileStep() {
        return this.stepBuilderFactory.get("chunkDBOneThreadToFileStep")
                .<Order, Order>chunk(10)
                .reader(jdbcCursorReader)
                .writer(fileItemWriterForOrder)
                .build();
    }

    @Bean
    public Job jobDBOneThreadToFile() {
        return this.jobBuilderFactory.get("jobDBOneThreadToFile")
                .start(chunkDBOneThreadToFileStep())
                .build();
    }
}
