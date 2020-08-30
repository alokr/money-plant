package dev.codethat.moneyplant.core.task;

import dev.codethat.moneyplant.core.analysis.technical.MarketTechnical;
import dev.codethat.moneyplant.core.analysis.technical.SuperTrendIndicator;
import dev.codethat.moneyplant.core.bean.data.MarketData;
import dev.codethat.moneyplant.core.bean.data.MarketTechnicals;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Data
@Slf4j
public class BarGeneratorTask implements Runnable {
    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    private final MarketData marketData;

    private final MarketTechnicals marketTechnicals;

    @Inject
    public BarGeneratorTask(MoneyPlantApplicationProperties moneyPlantApplicationProperties
            , MarketData marketData
            , MarketTechnicals marketTechnicals) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
        this.marketData = marketData;
        this.marketTechnicals = marketTechnicals;
    }

    @Override
    public void run() {
        if (!marketData.getMoneyPlantTickList().isEmpty()) {
            // compute bar
            MoneyPlantBar moneyPlantBar = marketData.computeBar();
            // add bar
            marketData.addBar(moneyPlantBar);
            // calculate market technical
            SuperTrendIndicator.Technical technical = marketTechnicals.getSuperTrendIndicator().calculateTechnical();
            log.info("upper={} lower={}", technical.getUpper(), technical.getLower());
        }
    }
}
