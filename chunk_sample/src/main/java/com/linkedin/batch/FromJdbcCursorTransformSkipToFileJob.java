package com.linkedin.batch;

import com.linkedin.batch.exception.OrderProcessingException;
import com.linkedin.batch.listener.CustomSkipListener;
import com.linkedin.batch.pojo.Order;
import com.linkedin.batch.pojo.TrackedOrder;
import com.linkedin.batch.transformer.FreeShippingItemProcessor;
import com.linkedin.batch.transformer.TrackedOrderItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class FromJdbcCursorTransformSkipToFileJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    ItemWriter<Order> stringItemWriter;

    @Autowired
    ItemWriter<TrackedOrder> fileItemWriterForTrackedOrder;

    @Autowired
    ItemReader<Order> jdbcCursorReader;

    @Autowired
    public ItemProcessor<Order, Order> orderValidatingItemProcessor;

    @Bean
    public Step chunkDBOneThreadTransformSkipToFileStep() {
        return this.stepBuilderFactory.get("chunkDBOneThreadTransformSkipToFileStep")
                .<Order, TrackedOrder>chunk(10)
                .reader(jdbcCursorReader)
                .processor(compositeItemProcessor())
                .faultTolerant()
                .skip(OrderProcessingException.class)
                .skipLimit(10)
                .listener(new CustomSkipListener())
                .writer(fileItemWriterForTrackedOrder)
                .build();
    }

    @Bean
    public ItemProcessor<Order, TrackedOrder> compositeItemProcessor() {
        return new CompositeItemProcessorBuilder<Order,TrackedOrder>()
                .delegates(orderValidatingItemProcessor, new TrackedOrderItemProcessor() , new FreeShippingItemProcessor())
                .build();
    }

    @Bean
    public Job jobDBOneThreadTransformSkipToFile() {
        return this.jobBuilderFactory.get("jobDBOneThreadTransformSkipToFile")
                .start(chunkDBOneThreadTransformSkipToFileStep())
                .build();
    }

}
