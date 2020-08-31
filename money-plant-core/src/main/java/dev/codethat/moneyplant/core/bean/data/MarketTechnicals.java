package dev.codethat.moneyplant.core.bean.data;

import dev.codethat.moneyplant.core.analysis.technical.SuperTrendIndicator;
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

@Named
@Data
@Slf4j
public class MarketTechnicals {
    private final MoneyPlantApplicationProperties properties;

    private final BarSeries barSeries;

    private final ATRIndicator atrIndicator;

    private final SuperTrendIndicator superTrendIndicator;

    @Inject
    public MarketTechnicals(MoneyPlantApplicationProperties moneyPlantApplicationProperties) {
        this.properties = moneyPlantApplicationProperties;
        this.barSeries = new BaseBarSeries();
        barSeries.setMaximumBarCount(this.properties.getTechnical()
                .getBarSeries().getBarCount());
        this.atrIndicator = new ATRIndicator(this.barSeries, this.properties.getTechnical()
                .getAtr().getBarCount());
        this.superTrendIndicator = new SuperTrendIndicator(properties.getTechnical().getSuperTrend().getMultiplier()
                , atrIndicator);
    }

    public void addBar(final MoneyPlantBar bar) {
        barSeries.addBar(
                ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                , bar.getOpenPrice()
                , bar.getHighPrice()
                , bar.getLowPrice()
                , bar.getClosePrice()
                , bar.getVolume());
        log.info("\nbar=[open={} low={} high={} close={} volume={}]"
                , bar.getOpenPrice(), bar.getLowPrice(), bar.getHighPrice(), bar.getClosePrice(), bar.getVolume());
    }
}
