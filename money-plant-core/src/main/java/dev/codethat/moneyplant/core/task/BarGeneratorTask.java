package dev.codethat.moneyplant.core.task;

import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.Data;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.ATRIndicator;

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
    private List<MoneyPlantTick> moneyPlantTickList = new ArrayList<>();

    private BarSeries barSeries;

    private ATRIndicator atrIndicator;

    private MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    @Override
    public void run() {
        List<MoneyPlantTick> lastBarTicks;
        synchronized (moneyPlantTickList) {
            lastBarTicks = new ArrayList<>(moneyPlantTickList);
        }
        MoneyPlantBar moneyPlantBar = new MoneyPlantBar();
        moneyPlantBar.setOpenPrice(lastBarTicks.get(0).getLastTradedPrice());
        moneyPlantBar.setClosePrice(lastBarTicks.get(lastBarTicks.size() - 1).getLastTradedPrice());

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
                    , moneyPlantBar.getClosePrice());
        }
        System.out.println(atrIndicator.getValue(barSeries.getEndIndex()));
    }
}
