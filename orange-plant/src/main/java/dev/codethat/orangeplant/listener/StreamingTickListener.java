package dev.codethat.orangeplant.listener;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Named
@Slf4j
public class StreamingTickListener implements StreamingTickCoreListener, OnTicks {
    private MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    private List<Double> tickList;

    private LoadingCache<Long, List<Double>> candleStickCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .removalListener((RemovalListener<Long, List<Double>>) removalNotification -> {
                System.out.println(removalNotification.getValue().get(0));
                tickList = new ArrayList<>();
                System.out.println("evicted");
            })
            .build(
                    new CacheLoader<>() {
                        @Override
                        public List<Double> load(Long instrumentToken) {
                            tickList = new ArrayList<>();
                            System.out.println("loaded");
                            return tickList;
                        }
                    }
            );

    @Inject
    public StreamingTickListener(MoneyPlantApplicationProperties moneyPlantApplicationProperties) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
    }

    private BarSeries barSeries;
    private ATRIndicator atrIndicator;

    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        ticks.stream().forEach(
                tick -> {
                    if (Objects.isNull(barSeries)) {
                        barSeries = new BaseBarSeries();
                        barSeries.setMaximumBarCount(moneyPlantApplicationProperties.getTechnical()
                                .getBarSeries().getBarCount());
                        atrIndicator = new ATRIndicator(barSeries, moneyPlantApplicationProperties.getTechnical()
                                .getAtr().getBarCount());

                        barSeries.addBar(
                                ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                                , tick.getOpenPrice()
                                , tick.getHighPrice()
                                , tick.getLowPrice()
                                , tick.getLastTradedPrice()
                                , tick.getVolumeTradedToday());

                        try {
                            candleStickCache.get(tick.getInstrumentToken());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    tickList.add(tick.getLastTradedPrice());
                    // candleStickCache.put(tick.getInstrumentToken(), Collections.singletonList(tick.getLastTradedPrice()));
                    // System.out.println(atrIndicator.getValue(barSeries.getEndIndex()));
                }
        );
    }
}
