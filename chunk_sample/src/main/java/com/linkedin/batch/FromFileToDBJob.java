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
public class FromFileToDBJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    ItemReader<Order> fileItemReaderForOrder;
    @Autowired
    ItemWriter<Order> stringItemWriter;
    @Autowired
    ItemWriter<Order> dbItemWriterForOrder;

    @Bean
    public Step chunkFileToDBStep() {
        return this.stepBuilderFactory.get("chunkFileToDBStep")
                .<Order, Order>chunk(10)
                .reader(fileItemReaderForOrder)
                .writer(dbItemWriterForOrder)
                .build();
    }

    @Bean
    public Job jobFromFileToDB() {
        return this.jobBuilderFactory.get("jobFromFileToDB")
                .start(chunkFileToDBStep())
                .build();
    }
}
