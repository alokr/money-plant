package dev.codethat.moneyplant.core.analysis.technical;

public interface TechnicalIndicator<T> {
    T getAverage(T... numbers);

    MarketTechnical calculateTechnical();
}
