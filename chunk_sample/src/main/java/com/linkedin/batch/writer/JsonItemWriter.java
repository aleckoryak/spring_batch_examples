package com.linkedin.batch.writer;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class JsonItemWriter {

    @Bean
    public ItemWriter<Order> jsonItemWriterForOrder() {
        return new JsonFileItemWriterBuilder<Order>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<Order>())
                .resource(new FileSystemResource("./data/shipped_orders_output.json"))
                .name("jsonItemWriterForOrder")
                .build();
    }
}
