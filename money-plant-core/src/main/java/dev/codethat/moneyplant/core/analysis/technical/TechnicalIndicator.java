package dev.codethat.moneyplant.core.analysis.technical;

import dev.codethat.moneyplant.core.bean.data.MoneyPlantBar;

import java.util.Optional;

public interface TechnicalIndicator<T> {
    T getAverage(T... numbers);

    Optional<MarketTechnical> calculateTechnical(final MoneyPlantBar currentBar);
}
