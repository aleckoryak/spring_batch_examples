package com.linkedin.batch.validation;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ValidatingItemProcessor {

    @Bean
    public ItemProcessor<Order, Order> orderValidatingItemProcessor() {
        BeanValidatingItemProcessor<Order> processor = new BeanValidatingItemProcessor<>();
        processor.setFilter(true);
        return processor;
    }
}
