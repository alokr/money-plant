package dev.codethat.orangeplant.listener;

import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.OnTicks;
import dev.codethat.moneyplant.core.listener.StreamingTickCoreListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class StreamingTickListener implements StreamingTickCoreListener, OnTicks {
    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        ticks.stream().forEach(
                tick -> {
                    log.info(String.valueOf(tick.getClosePrice()));
                    log.info(String.valueOf(tick.getOpenPrice()));
                    log.info(String.valueOf(tick.getLowPrice()));
                    log.info(String.valueOf(tick.getHighPrice()));
                    log.info(String.valueOf(tick.getLastTradedPrice()));
                    log.info(String.valueOf(tick.getLastTradedTime()));
                }
        );
    }
}
