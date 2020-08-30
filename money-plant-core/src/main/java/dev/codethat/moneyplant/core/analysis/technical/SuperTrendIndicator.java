package dev.codethat.moneyplant.core.analysis.technical;

import lombok.Data;
import org.ta4j.core.indicators.ATRIndicator;

public class SuperTrendIndicator extends DoubleAbstractIndicator {
    private final double multiplier;

    private final ATRIndicator atrIndicator;

    public SuperTrendIndicator(double multiplier, ATRIndicator atrIndicator) {
        this.multiplier = multiplier;
        this.atrIndicator = atrIndicator;
    }

    @Override
    public Technical calculateTechnical() {
        double avg = getAverage(currentBar.getHighPrice(), currentBar.getLowPrice());
        double mATR = multiplier * atrIndicator.getValue(atrIndicator.getBarSeries().getEndIndex()).doubleValue();

        Technical technical = new Technical();
        technical.upper = avg + mATR;
        technical.lower = avg - mATR;
        return technical;
    }

    @Data
    public class Technical implements MarketTechnical {
        private double upper;
        private double lower;
    }
}
