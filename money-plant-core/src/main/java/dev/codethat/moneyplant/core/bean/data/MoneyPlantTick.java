package dev.codethat.moneyplant.core.bean.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoneyPlantTick {
    private double lastTradedPrice;

    private double tradeVolume;
}
