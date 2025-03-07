package com.linkedin.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkedin.batch.mapper.OrderRowMapper;
import com.linkedin.batch.pojo.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class FromJdbcPagingToStringJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;


    public JdbcPagingItemReader<Order> jdbcPagingItemReader() throws Exception {
        JdbcPagingItemReader<Order> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setFetchSize(10);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();

        queryProvider.setSelectClause("select order_id, first_name, last_name, email, cost, item_id, item_name, ship_date");
        queryProvider.setFromClause("from SHIPPED_ORDER");
        itemReader.setRowMapper(new OrderRowMapper());
        final Map<String, org.springframework.batch.item.database.Order> sortKeys = new HashMap<>();
        itemReader.setParameterValues(new HashMap<>());
        sortKeys.put("order_id", org.springframework.batch.item.database.Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        itemReader.setQueryProvider(queryProvider);
        itemReader.afterPropertiesSet();
        return itemReader;
    }

    @Bean
    public Step fromJdbcPagingToStringStep() throws Exception {
        return this.stepBuilderFactory.get("fromJdbcPagingToStringStep")
                .<Order, Order>chunk(10)
                .reader(jdbcPagingItemReader())
                .writer(new ItemWriter<Order>() {

                    @Override
                    public void write(List<? extends Order> items) throws Exception {
                        System.out.println(String.format("Received list of size: %s", items.size()));
                        items.forEach(System.out::println);
                    }

                }).build();
    }

    @Bean
    public Job fromJdbcPagingToStringJob() throws Exception {
        return this.jobBuilderFactory.get("fromJdbcPagingToStringJob").start(fromJdbcPagingToStringStep()).build();
    }

}
