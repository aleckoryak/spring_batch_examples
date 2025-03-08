package com.linkedin.batch;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component
public class FromFileToStringJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    ItemReader<Order> fileItemReader;
    @Autowired
    ItemWriter<Order> stringItemWriter;

    @Bean
    public Step chunkFileToStringStep() {
        return this.stepBuilderFactory.get("chunkFileToStringStep")
                .<Order, Order>chunk(10)
                .reader(fileItemReader)
                .writer(stringItemWriter)
                .build();
    }

    @Bean
    public Job jobFromFileToString() {
        return this.jobBuilderFactory.get("jobFromFileToString")
                .start(chunkFileToStringStep())
                .build();
    }
}
