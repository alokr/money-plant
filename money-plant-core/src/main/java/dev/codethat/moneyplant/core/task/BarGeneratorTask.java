package dev.codethat.moneyplant.core.task;

import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.Data;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Named
@Data
@Slf4j
public class BarGeneratorTask implements Runnable {
    private final List<MoneyPlantTick> moneyPlantTickList = new ArrayList<>();

    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    @Inject
    public BarGeneratorTask(MoneyPlantApplicationProperties moneyPlantApplicationProperties) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;

        barSeries = new BaseBarSeries();
        barSeries.setMaximumBarCount(moneyPlantApplicationProperties.getTechnical()
                .getBarSeries().getBarCount());
        atrIndicator = new ATRIndicator(barSeries, moneyPlantApplicationProperties.getTechnical()
                .getAtr().getBarCount());

        if (moneyPlantApplicationProperties.getMarketData().isRunTickSimulator()) {
            CompletableFuture.runAsync(() -> {
                double ltp = 24600;
                double volume = 8473300;
                while (true) {
                    boolean run = true;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    int sign = 2;
                    while (run) {
                        synchronized (moneyPlantTickList) {
                            if (sign % 2 == 0) {
                                ltp += ThreadLocalRandom.current().nextDouble(1, 25);
                                sign += 1;
                            } else {
                                ltp -= ThreadLocalRandom.current().nextDouble(1, 25);
                                sign -= 1;
                            }
                            volume += ThreadLocalRandom.current().nextDouble(100, 500);
                            moneyPlantTickList
                                    .add(new MoneyPlantTick(ltp, volume));
                            log.debug("simulation: ltp={}, volume={}", ltp, volume);
                            run = false;
                        }
                    }
                }
            });
        }
    }

    private BarSeries barSeries;

    private ATRIndicator atrIndicator;

    @Override
    public void run() {
        if (!moneyPlantTickList.isEmpty()) {
            List<MoneyPlantTick> lastBarTicks;
            synchronized (moneyPlantTickList) {
                lastBarTicks = new ArrayList<>(moneyPlantTickList);
                log.debug("lastBarTicks={} mpTicks={}", lastBarTicks.size(), moneyPlantTickList.size());
                moneyPlantTickList.clear();
            }
            MoneyPlantBar moneyPlantBar = new MoneyPlantBar();
            moneyPlantBar.setOpenPrice(lastBarTicks.get(0).getLastTradedPrice());
            moneyPlantBar.setClosePrice(lastBarTicks.get(lastBarTicks.size() - 1).getLastTradedPrice());
            moneyPlantBar.setLowPrice(
                    lastBarTicks.stream()
                            .mapToDouble(MoneyPlantTick::getLastTradedPrice)
                            .min()
                            .getAsDouble());
            moneyPlantBar.setHighPrice(
                    lastBarTicks.stream()
                            .mapToDouble(MoneyPlantTick::getLastTradedPrice)
                            .max()
                            .getAsDouble());
            moneyPlantBar.setVolume(lastBarTicks.stream()
                    .mapToDouble(MoneyPlantTick::getTradeVolume)
                    .average().getAsDouble());

            barSeries.addBar(
                    ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                    , moneyPlantBar.getOpenPrice()
                    , moneyPlantBar.getHighPrice()
                    , moneyPlantBar.getLowPrice()
                    , moneyPlantBar.getClosePrice()
                    , moneyPlantBar.getVolume());
            lastBarTicks.clear();
            log.info("bar: open={} low={} high={} close={} volume={}"
                    , moneyPlantBar.getOpenPrice(), moneyPlantBar.getLowPrice()
                    , moneyPlantBar.getHighPrice(), moneyPlantBar.getClosePrice(), moneyPlantBar.getVolume());
            log.info("atr={} bars={}", atrIndicator.getValue(atrIndicator.getBarSeries().getEndIndex())
                    , barSeries.getBarCount());
        }
    }
}
