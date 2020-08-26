package dev.codethat.moneyplant.core.task;

import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.Data;
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
import java.util.Objects;

@Named
@Data
public class BarGeneratorTask implements Runnable {
    private final List<MoneyPlantTick> moneyPlantTickList = new ArrayList<>();

    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    @Inject
    public BarGeneratorTask(MoneyPlantApplicationProperties moneyPlantApplicationProperties) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
    }

    private BarSeries barSeries;

    private ATRIndicator atrIndicator;

    @Override
    public void run() {
        if (!moneyPlantTickList.isEmpty()) {
            List<MoneyPlantTick> lastBarTicks;
            synchronized (moneyPlantTickList) {
                lastBarTicks = new ArrayList<>(moneyPlantTickList);
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

            if (Objects.isNull(barSeries)) {
                barSeries = new BaseBarSeries();
                barSeries.setMaximumBarCount(moneyPlantApplicationProperties.getTechnical()
                        .getBarSeries().getBarCount());
                atrIndicator = new ATRIndicator(barSeries, moneyPlantApplicationProperties.getTechnical()
                        .getAtr().getBarCount());

            barSeries.addBar(
                        ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                        , moneyPlantBar.getOpenPrice()
                        , moneyPlantBar.getHighPrice()
                        , moneyPlantBar.getLowPrice()
                        , moneyPlantBar.getClosePrice()
                        , moneyPlantBar.getVolume());
            }
            System.out.println(atrIndicator.getValue(barSeries.getEndIndex()));
        }
    }
}
