package dev.codethat.orangeplant.adapter;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import dev.codethat.moneyplant.core.adapter.OrderAdapter;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import dev.codethat.orangeplant.bean.request.OrderRequestTO;
import dev.codethat.orangeplant.bean.response.OrderResponseTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.io.IOException;

@Named
@Slf4j
@AllArgsConstructor
public class OrderAdapterImpl implements OrderAdapter<OrderRequestTO, OrderResponseTO> {
    private final OrangePlantApplicationProperties applicationProperties;

    private final MoneyPlantCache moneyPlantCache;

    @Override
    public OrderResponseTO place(OrderRequestTO requestTO) throws Exception {
        KiteConnect kiteConnect = moneyPlantCache.brokerHttpClient();
        OrderResponseTO orderResponseTO = null;
        try {
            orderResponseTO.setOrder(kiteConnect.placeOrder(requestTO.getOrderParams(), Constants.VARIETY_REGULAR));
        } catch (KiteException | IOException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
            throw new Exception(e);
        }
        return orderResponseTO;
    }

    @Override
    public OrderResponseTO replace(OrderRequestTO requestTO) {
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
