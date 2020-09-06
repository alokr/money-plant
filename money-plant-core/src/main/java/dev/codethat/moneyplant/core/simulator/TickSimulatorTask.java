package dev.codethat.moneyplant.core.simulator;

import dev.codethat.moneyplant.core.bean.data.MarketData;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Named
@Slf4j
public class TickSimulatorTask implements Runnable {
    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;
    private final MarketData marketData;
    private double lastTradedPrice;
    private double volume;

    @Inject
    public TickSimulatorTask(MoneyPlantApplicationProperties moneyPlantApplicationProperties, MarketData marketData) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
        this.marketData = marketData;
        this.lastTradedPrice = moneyPlantApplicationProperties.getMarketSimulation().getLastTradedPrice();
        this.volume = moneyPlantApplicationProperties.getMarketSimulation().getVolumeTraded();
    }

    @Override
    public void run() {
        lastTradedPrice = lastTradedPrice + 25;
        volume = volume + 20;
        marketData.addTick(new MoneyPlantTick(lastTradedPrice, volume));
        log.info("ltp={}, volume={}", lastTradedPrice, volume);
    }
}
