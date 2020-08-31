package dev.codethat.orangeplant.listener;

import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.OnTicks;
import dev.codethat.moneyplant.core.bean.data.MarketData;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.listener.StreamingTickCoreListener;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
@Slf4j
public class StreamingTickListener implements StreamingTickCoreListener, OnTicks {
    private final MarketData marketData;

    @Inject
    public StreamingTickListener(MarketData marketData) {
        this.marketData = marketData;
    }

    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        ticks.stream().forEach(
                tick -> {
                    marketData.addTick(new MoneyPlantTick(tick.getLastTradedPrice(), tick.getVolumeTradedToday()));
                    log.debug("tickCount={} ltp={}", ticks.size(), tick.getLastTradedPrice());
                }
        );
    }
}
