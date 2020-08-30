package dev.codethat.moneyplant.core.analysis.technical;

import java.util.Arrays;

public abstract class DoubleAbstractIndicator implements TechnicalIndicator<Double> {
    @Override
    public Double getAverage(Double... numbers) {
        return Arrays.stream(numbers).mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }
}
