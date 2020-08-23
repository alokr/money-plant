package dev.codethat.orangeplant.listener;

import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.OnTicks;
import dev.codethat.moneyplant.core.listener.StreamingTickCoreListener;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.ATRIndicator;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Objects;

@Named
@Slf4j
public class StreamingTickListener implements StreamingTickCoreListener, OnTicks {
    private MoneyPlantApplicationProperties coreApplicationProperties;

    @Inject
    public StreamingTickListener(MoneyPlantApplicationProperties moneyPlantApplicationProperties) {
        this.coreApplicationProperties = moneyPlantApplicationProperties;
    }

    private BarSeries barSeries;
    private ATRIndicator atrIndicator;

    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        ticks.stream().forEach(
                tick -> {
                    if (Objects.isNull(barSeries)) {
                        barSeries = new BaseBarSeries();
                        barSeries.setMaximumBarCount(coreApplicationProperties.getTechnical()
                                .getBarSeries().getBarCount());
                        atrIndicator = new ATRIndicator(barSeries, coreApplicationProperties.getTechnical()
                                .getAtr().getBarCount());
                    }
                    barSeries.addBar(
                            ZonedDateTime.ofInstant(tick.getLastTradedTime().toInstant(), ZoneId.systemDefault())
//                            ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())
                            , tick.getOpenPrice()
                            , tick.getHighPrice()
                            , tick.getLowPrice()
                            , tick.getLastTradedPrice()
                            , tick.getVolumeTradedToday());

                    barSeries.addTrade(tick.getVolumeTradedToday(), tick.getLastTradedPrice());
                    System.out.println(atrIndicator.getValue(barSeries.getEndIndex()));
                }
        );
    }
}
