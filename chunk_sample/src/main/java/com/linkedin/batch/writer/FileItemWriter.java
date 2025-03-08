package com.linkedin.batch.writer;

import com.linkedin.batch.pojo.Order;
import com.linkedin.batch.pojo.TrackedOrder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class FileItemWriter {
    public static final String[] ORDER_NAMES = {"orderId", "firstName", "lastName", "email", "cost", "itemId", "itemName", "shipDate"};
    public static final String[] TRACKED_ORDER_NAMES = {"orderId", "firstName", "lastName", "email", "cost", "itemId", "itemName", "shipDate", "trackingNumber", "freeShipping"};

    @Bean
    public ItemWriter<Order> fileItemWriterForOrder() {
        BeanWrapperFieldExtractor<Order> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(ORDER_NAMES);

        DelimitedLineAggregator<Order> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(extractor);


        FlatFileItemWriter<Order> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("./data/shipped_orders_output.csv"));
        writer.setLineAggregator(lineAggregator);
        return writer;
    }

    @Bean
    public ItemWriter<TrackedOrder> fileItemWriterForTrackedOrder() {
        BeanWrapperFieldExtractor<TrackedOrder> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(TRACKED_ORDER_NAMES);

        DelimitedLineAggregator<TrackedOrder> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(extractor);


        FlatFileItemWriter<TrackedOrder> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("./data/tracked_orders_output.csv"));
        writer.setLineAggregator(lineAggregator);
        return writer;
    }
}
