package com.linkedin.batch.listener;

import com.linkedin.batch.pojo.Order;
import com.linkedin.batch.pojo.TrackedOrder;
import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<Order, TrackedOrder> {
    @Override
    public void onSkipInRead(Throwable throwable) {

    }

    @Override
    public void onSkipInWrite(TrackedOrder trackedOrder, Throwable throwable) {

    }

    @Override
    public void onSkipInProcess(Order order, Throwable throwable) {
        System.out.println("skipping  processing of " + order.getOrderId());
    }
}
