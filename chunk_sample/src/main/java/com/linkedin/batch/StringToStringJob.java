package com.linkedin.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component
public class StringToStringJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    List<String> list = Arrays.asList("s1","s2","s3","s4","s5","s6","s7","s8","s9","s10");
    Iterator<String> iterator = list.iterator();

    @Bean
    public ItemReader<String> itemReader() {
        return new ItemReader<String>() {
            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                return iterator.hasNext() ? iterator.next() : null;
            }
        };
    }

    @Bean
    public Step chunkStringToStringStep() {
        return this.stepBuilderFactory.get("chunkStringToStringStep")
                .<String, String>chunk(3)
                .reader(itemReader())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> list) throws Exception {
                        System.out.println(String.format("Received list of size %d", list.size()));
                        list.forEach(System.out::println);

                    }
                })
                .build();
    }

    @Bean
    public Job jobStringToString() {
        return this.jobBuilderFactory.get("jobStringToString")
                .start(chunkStringToStringStep())
                .build();
    }
}
