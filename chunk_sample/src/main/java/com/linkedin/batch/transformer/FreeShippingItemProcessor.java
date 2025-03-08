package com.linkedin.batch.transformer;

import com.linkedin.batch.pojo.Order;
import com.linkedin.batch.pojo.TrackedOrder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class FreeShippingItemProcessor implements ItemProcessor<TrackedOrder, TrackedOrder> {
    @Override
    public TrackedOrder process(TrackedOrder order) throws Exception {
        order.setFreeShipping(order.getCost().compareTo(new BigDecimal("80")) == 1);
        return order.isFreeShipping()?order:null;
    }
}
