package com.linkedin.batch.reader;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DbItemReader {
    public static String ORDER_SQL = "select order_id, first_name, last_name, "
            + "email, cost, item_id, item_name, ship_date "
            + "from SHIPPED_ORDER order by order_id";

    @Autowired
    DataSource dataSource;

    @Bean
    public ItemReader<Order> jdbcCursorReader() {
        return new JdbcCursorItemReaderBuilder<Order>()
                .dataSource(dataSource)
                .name("JdbcCursorItemReader")
                .sql(ORDER_SQL)
                .rowMapper(new OrderRowMapper())
                .build();
    }

    @Bean
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


}

class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getLong("order_id"));
        order.setCost(rs.getBigDecimal("cost"));
        order.setEmail(rs.getString("email"));
        order.setFirstName(rs.getString("first_name"));
        order.setLastName(rs.getString("last_name"));
        order.setItemId(rs.getString("item_id"));
        order.setItemName(rs.getString("item_name"));
        order.setShipDate(rs.getDate("ship_date"));
        return order;
    }
}
