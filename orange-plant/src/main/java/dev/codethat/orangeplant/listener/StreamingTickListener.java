package dev.codethat.orangeplant.listener;

import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.OnTicks;
import dev.codethat.moneyplant.core.listener.StreamingTickCoreListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class StreamingTickListener implements StreamingTickCoreListener, OnTicks {
    private BarSeries barSeries;
    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        ticks.stream().forEach(
                tick -> {
                    if (Objects.isNull(barSeries)) {
                        barSeries = new BaseBarSeries();
                        barSeries.setMaximumBarCount(7);
                        barSeries.addBar(
//                                ZonedDateTime.ofInstant(tick.getLastTradedTime().toInstant(), ZoneId.systemDefault())
                                ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())
                                , tick.getOpenPrice()
                                , tick.getHighPrice()
                                , tick.getLowPrice()
                                , tick.getClosePrice()
                                , tick.getVolumeTradedToday()
                        );
                    } else {
                        barSeries.addTrade(tick.getVolumeTradedToday(), tick.getClosePrice());
                    }
                    System.out.println(barSeries.toString());
                }
        );
    }
}
