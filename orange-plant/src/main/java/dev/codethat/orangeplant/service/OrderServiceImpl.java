package dev.codethat.orangeplant.service;

import dev.codethat.moneyplant.core.adapter.OrderAdapter;
import dev.codethat.moneyplant.core.service.OrderService;
import dev.codethat.orangeplant.bean.request.OrderRequestTO;
import dev.codethat.orangeplant.bean.response.OrderResponseTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService<OrderRequestTO, OrderResponseTO> {
    private OrderAdapter orderAdapter;

    @Override
    public OrderResponseTO place(OrderRequestTO requestTO) throws Exception {
        return (OrderResponseTO) orderAdapter.place(requestTO);
    }

    @Override
    public OrderResponseTO replace(OrderRequestTO requestTO) throws Exception {
        return null;
    }

    @Override
    public OrderResponseTO cancel(OrderRequestTO requestTO) throws Exception {
        return null;
    }

    @Override
    public OrderResponseTO orders(OrderRequestTO requestTO) {
        return null;
    }
}
