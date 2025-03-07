package com.linkedin.batch;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FromJdbcCursorToStringJob {
    public static final String[] tokens = {"order_id", "first_name", "last_name", "email", "cost", "item_id", "item_name", "ship_date"};
    public static String ORDER_SQL = "select order_id, first_name, last_name, "
            + "email, cost, item_id, item_name, ship_date "
            + "from SHIPPED_ORDER order by order_id";

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    DataSource dataSource;


    @Bean
    public ItemReader<Order> jdbcCursorReader() {
        return new JdbcCursorItemReaderBuilder<Order>()
                .dataSource(dataSource)
                .name("JdbcCursorItemReader")
                .sql(ORDER_SQL)
                .rowMapper(new RowMapper<Order>() {
                    @Override
                    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
                        Order order = new Order();
                        order.setOrderId(resultSet.getLong("order_id"));
                        order.setCost(resultSet.getBigDecimal("cost"));
                        order.setEmail(resultSet.getString("email"));
                        order.setFirstName(resultSet.getString("first_name"));
                        order.setLastName(resultSet.getString("last_name"));
                        order.setItemId(resultSet.getString("item_id"));
                        order.setItemName(resultSet.getString("item_name"));
                        order.setShipDate(resultSet.getDate("ship_date"));
                        return order;
                    }
                })
                .build();

    }

    @Bean
    public Step chunkDBOneThreadToStringStep() {
        return this.stepBuilderFactory.get("chunkDBOneThreadToStringStep")
                .<Order, Order>chunk(10)
                .reader(jdbcCursorReader())
                .writer(new ItemWriter<Order>() {
                    @Override
                    public void write(List<? extends Order> list) throws Exception {
                        System.out.printf("Received list of size %d%n", list.size());
                        list.forEach(System.out::println);

                    }
                })
                .build();
    }

    @Bean
    public Job jobDBOneThreadToString() {
        return this.jobBuilderFactory.get("jobDBOneThreadToString")
                .start(chunkDBOneThreadToStringStep())
                .build();
    }
}
