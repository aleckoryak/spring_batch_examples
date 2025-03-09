package com.linkedin.batch.reader;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
public class FileItemReader {
    public static final String[] tokens = {"order_id", "first_name", "last_name", "email", "cost", "item_id", "item_name", "ship_date"};


    @Bean
    public ItemReader<Order> fileItemReaderForOrder() {
        //1 tokenizer
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames(tokens);

        //2 mapper
        DefaultLineMapper<Order> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FieldSetMapper<Order>() {
            @Override
            public Order mapFieldSet(FieldSet fieldSet) throws BindException {
                Order order = new Order();
                order.setOrderId(fieldSet.readLong("order_id"));
                order.setCost(fieldSet.readBigDecimal("cost"));
                order.setEmail(fieldSet.readString("email"));
                order.setFirstName(fieldSet.readString("first_name"));
                order.setLastName(fieldSet.readString("last_name"));
                order.setItemId(fieldSet.readString("item_id"));
                order.setItemName(fieldSet.readString("item_name"));
                order.setShipDate(fieldSet.readDate("ship_date"));
                return order;
            }
        });

        FlatFileItemReader<Order> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource("./data/shipped_orders.csv"));
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
