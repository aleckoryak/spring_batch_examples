package com.linkedin.batch;

import com.linkedin.batch.pojo.Order;
import com.linkedin.batch.pojo.TrackedOrder;
import com.linkedin.batch.transformer.TrackedOrderItemProcessor;
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

import java.util.List;
import java.util.UUID;


@Component
public class FromJdbcCursorTransformToFileJob {
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
    public Step chunkDBOneThreadTransformToFileStep() {
        return this.stepBuilderFactory.get("chunkDBOneThreadTransformToFileStep")
                .<Order, TrackedOrder>chunk(10)
                .reader(jdbcCursorReader)
                .processor(new TrackedOrderItemProcessor())
                .writer(fileItemWriterForTrackedOrder)
                .build();
    }

//    @Bean
//    public ItemProcessor<Order, TrackedOrder> trackedOrderTransformItemProcessor() {
//        return new TrackedOrderItemProcessor();
//    }


    @Bean
    public Job jobDBOneThreadTransformToFile() {
        return this.jobBuilderFactory.get("jobDBOneThreadTransformToFile")
                .start(chunkDBOneThreadTransformToFileStep())
                .build();
    }

//    class TrackedOrderItemProcessor implements ItemProcessor<Order, TrackedOrder> {
//        @Override
//        public TrackedOrder process(Order order) throws Exception {
//            TrackedOrder trackedOrder = new TrackedOrder(order);
//            trackedOrder.setTrackingNumber(UUID.randomUUID().toString());
//            trackedOrder.setFreeShipping(false);
//            return trackedOrder;
//        }
//    }
}
