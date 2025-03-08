package com.linkedin.batch.writer;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StringWriterToConsole {

    @Bean
    public ItemWriter<Order> stringItemWriter() {
        return new ItemWriter<Order>() {
            @Override
            public void write(List<? extends Order> list) throws Exception {
                System.out.println(String.format("Received list of size %d", list.size()));
                list.forEach(System.out::println);
            }
        };
    }

}
