package com.linkedin.batch;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class FromJdbcCursorValidateToFileJob {
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

    @Autowired
    public ItemProcessor<Order, Order> orderValidatingItemProcessor;

    @Bean
    public Step chunkDBOneThreadValidateToFileStep() {
        return this.stepBuilderFactory.get("chunkDBOneThreadValidateToFileStep")
                .<Order, Order>chunk(10)
                .reader(jdbcCursorReader)
                .processor(orderValidatingItemProcessor)
                .writer(fileItemWriterForOrder)
                .build();
    }



    @Bean
    public Job jobDBOneThreadValidateToFile() {
        return this.jobBuilderFactory.get("jobDBOneThreadValidateToFile")
                .start(chunkDBOneThreadValidateToFileStep())
                .build();
    }
}
