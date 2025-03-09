package com.linkedin.batch.transformer;

import com.linkedin.batch.exception.OrderProcessingException;
import com.linkedin.batch.pojo.Order;
import com.linkedin.batch.pojo.TrackedOrder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackedOrderItemProcessor implements ItemProcessor<Order, TrackedOrder> {
    @Override
    public TrackedOrder process(Order order) throws Exception {
        System.out.println("Processing order: " + order.getOrderId());
        TrackedOrder trackedOrder = new TrackedOrder(order);
        trackedOrder.setTrackingNumber(getTrackingNumber());
        return trackedOrder;
    }

    private String getTrackingNumber() throws OrderProcessingException {
        if (Math.random() < .10) {
            throw new OrderProcessingException();
        }
        return UUID.randomUUID().toString();
    }
}
