package com.linkedin.batch.transformer;

import com.linkedin.batch.pojo.Order;
import com.linkedin.batch.pojo.TrackedOrder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackedOrderItemProcessor implements ItemProcessor<Order, TrackedOrder> {
    @Override
    public TrackedOrder process(Order order) throws Exception {
        TrackedOrder trackedOrder = new TrackedOrder(order);
        trackedOrder.setTrackingNumber(UUID.randomUUID().toString());
        return trackedOrder;
    }
}
