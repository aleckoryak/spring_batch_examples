package com.linkedin.batch.writer;

import com.linkedin.batch.pojo.Order;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class DbItemWriter {
    @Autowired
    DataSource dataSource;

    public static String INSERT_ORDER_SQL = "insert into "
            + "SHIPPED_ORDER_OUTPUT(order_id, first_name, last_name, email, item_id, item_name, cost, ship_date)"
            + " values(?,?,?,?,?,?,?,?)";

    @Bean
    public ItemWriter<Order> dbItemWriterForOrder() {
        return new JdbcBatchItemWriterBuilder<Order>()
                .dataSource(dataSource)
                .sql(INSERT_ORDER_SQL)
                .itemPreparedStatementSetter(new OrderItemPrepearedStatmentSetter())
                .build();
    }
}

class OrderItemPrepearedStatmentSetter implements ItemPreparedStatementSetter<Order> {
    @Override
    public void setValues(Order order, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, order.getOrderId());
        preparedStatement.setString(2, order.getFirstName());
        preparedStatement.setString(3, order.getLastName());
        preparedStatement.setString(4, order.getEmail());
        preparedStatement.setString(5, order.getItemId());
        preparedStatement.setString(6, order.getItemName());
        preparedStatement.setBigDecimal(7, order.getCost());
        preparedStatement.setDate(8, new Date(order.getShipDate().getTime()));
    }
}
