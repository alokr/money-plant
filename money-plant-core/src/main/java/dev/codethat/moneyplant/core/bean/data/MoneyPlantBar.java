package dev.codethat.moneyplant.core.bean.data;

import lombok.Data;

@Data
public class MoneyPlantBar {
    private double openPrice;

    private double closePrice;

    private double highPrice;

    private double lowPrice;

    private double volume;

    private double ltp;
}
