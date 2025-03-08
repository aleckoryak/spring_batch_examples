package com.linkedin.batch;


import com.linkedin.batch.pojo.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class FromJdbcCursorToJsonJob {
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
    public ItemWriter<Order> jsonItemWriterForOrder;

    @Bean
    public Step chunkDBOneThreadToJsonStep() {
        return this.stepBuilderFactory.get("chunkDBOneThreadToJsonStep")
                .<Order, Order>chunk(10)
                .reader(jdbcCursorReader)
                .writer(jsonItemWriterForOrder)
                .build();
    }

    @Bean
    public Job jobDBOneThreadToJson() {
        return this.jobBuilderFactory.get("jobDBOneThreadToJson")
                .start(chunkDBOneThreadToJsonStep())
                .build();
    }
}
