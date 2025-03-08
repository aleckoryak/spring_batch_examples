package com.linkedin.batch.writer;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class FileItemWriter {
    public static final String[] names = {"orderId", "firstName", "lastName", "email", "cost", "itemId", "itemName", "shipDate"};

    @Bean
    public ItemWriter<Order> fileItemWriterForOrder() {
        BeanWrapperFieldExtractor<Order> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(names);

        DelimitedLineAggregator<Order> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(extractor);


        FlatFileItemWriter<Order> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("./data/shiped_orders_output.csv"));
        writer.setLineAggregator(lineAggregator);
        return writer;
    }
}
