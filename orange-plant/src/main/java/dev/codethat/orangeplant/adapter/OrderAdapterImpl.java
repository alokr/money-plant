package dev.codethat.orangeplant.adapter;

import dev.codethat.moneyplant.core.adapter.OrderAdapter;
import dev.codethat.orangeplant.to.request.OrderRequestTO;
import dev.codethat.orangeplant.to.response.OrderResponseTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Named
@Slf4j
public class OrderAdapterImpl implements OrderAdapter<OrderRequestTO, OrderResponseTO> {

    @Override
    public OrderResponseTO place(OrderRequestTO requestTO) {
        return null;
    }

    @Override
    public OrderResponseTO replace(OrderRequestTO requestTO) {
        return null;
    }

    @Override
    public OrderResponseTO order(OrderRequestTO requestTO) {
        return null;
    }

    @Override
    public OrderResponseTO cancel(OrderRequestTO requestTO) {
        return null;
    }

    @Override
    public OrderResponseTO orders(OrderRequestTO requestTO) {
        return null;
    }
}
