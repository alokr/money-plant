package dev.codethat.moneyplant.core.analysis.technical;

import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;

import java.util.Arrays;

public abstract class DoubleAbstractIndicator implements TechnicalIndicator<Double> {
    protected MoneyPlantBar currentBar;

    @Override
    public Double getAverage(Double... numbers) {
        return Arrays.stream(numbers).mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }
}
