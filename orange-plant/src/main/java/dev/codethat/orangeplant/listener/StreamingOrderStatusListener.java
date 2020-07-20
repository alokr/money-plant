package dev.codethat.orangeplant.listener;

import com.zerodhatech.models.Order;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.OnOrderUpdate;
import com.zerodhatech.ticker.OnTicks;
import dev.codethat.moneyplant.core.listener.StreamingOrderStatusCoreListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class StreamingOrderStatusListener implements StreamingOrderStatusCoreListener, OnOrderUpdate {
    @Override
    public void onOrderUpdate(Order order) {

    }
}
