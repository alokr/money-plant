package dev.codethat.moneyplant.core.analysis.technical;

import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.indicators.ATRIndicator;

import java.util.Optional;

@Slf4j
public class SuperTrendIndicator extends DoubleAbstractIndicator {
    private final double multiplier;

    private final ATRIndicator atrIndicator;

    public SuperTrendIndicator(double multiplier, ATRIndicator atrIndicator) {
        this.multiplier = multiplier;
        this.atrIndicator = atrIndicator;
    }

    @Override
    public Optional<MarketTechnical> calculateTechnical(final MoneyPlantBar currentBar) {
        if (atrIndicator.getBarSeries().getBarCount() < atrIndicator.getBarSeries().getMaximumBarCount()) {
            log.info("bars={}", atrIndicator.getBarSeries().getBarCount());
            return Optional.empty();
        }
        double avg = getAverage(currentBar.getHighPrice(), currentBar.getLowPrice());
        double mATR = multiplier * atrIndicator.getValue(atrIndicator.getBarSeries().getEndIndex()).doubleValue();

        Technical technical = new Technical();
        technical.upper = avg + mATR;
        technical.lower = avg - mATR;
        return Optional.of(technical);
    }

    @Data
    public class Technical implements MarketTechnical {
        private double upper;
        private double lower;
    }
}
