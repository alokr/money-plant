package dev.codethat.moneyplant.core.task;

import dev.codethat.moneyplant.core.analysis.technical.MarketTechnical;
import dev.codethat.moneyplant.core.analysis.technical.SuperTrendIndicator;
import dev.codethat.moneyplant.core.bean.data.MarketData;
import dev.codethat.moneyplant.core.bean.data.MarketTechnicals;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.Optional;

@Named
@Data
@Slf4j
public class BarGeneratorTask implements Runnable {
    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    private final MarketData marketData;

    private final MarketTechnicals marketTechnicals;

    public double boughtPrice;

    public double soldPrice;

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
            // calculate technical
            Optional<MarketTechnical> technical = marketTechnicals.getSuperTrendIndicator()
                    .calculateTechnical(moneyPlantBar);
            if (technical.isPresent()) {
                SuperTrendIndicator.Technical marketTechnical = (SuperTrendIndicator.Technical) technical.get();
                log.info("lower={} ltp={} upper={}"
                        , marketTechnical.getLower(), moneyPlantBar.getLtp(), marketTechnical.getUpper());
                if (moneyPlantBar.getLtp() > marketTechnical.getUpper()) {
                    log.info("buy at {}", moneyPlantBar.getLtp());
                    boughtPrice = moneyPlantBar.getLtp();
                }
                if (moneyPlantBar.getLtp() < marketTechnical.getLower()) {
                    log.info("sell at {}", moneyPlantBar.getLtp());
                    soldPrice = moneyPlantBar.getLtp();
                }
            }
        }
    }
}
