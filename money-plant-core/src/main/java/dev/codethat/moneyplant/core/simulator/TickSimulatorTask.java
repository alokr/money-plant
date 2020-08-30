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
    @Inject
    public TickSimulatorTask(MoneyPlantApplicationProperties moneyPlantApplicationProperties, MarketData marketData) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
        this.marketData = marketData;
    }

    @Override
    public void run() {
        double lastTradedPrice = moneyPlantApplicationProperties.getMarketSimulation().getLastTradedPrice();
        long volume = moneyPlantApplicationProperties.getMarketSimulation().getVolumeTraded();
        if (new Random(new Date().getTime()).nextInt() % 2 == 0) {
            lastTradedPrice += ThreadLocalRandom.current().nextDouble(5, 50);
        } else {
            lastTradedPrice -= ThreadLocalRandom.current().nextDouble(5, 50);
        }
        volume += ThreadLocalRandom.current().nextDouble(10, 1000);
        marketData.addTick(new MoneyPlantTick(lastTradedPrice, volume));
        log.debug("ltp={}, volume={}", lastTradedPrice, volume);
    }
}
