package dev.codethat.moneyplant.core.bean.data;

import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Named
@Data
@Slf4j
public class MarketData {
    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;
    // data
    private final List<MoneyPlantTick> moneyPlantTickList;

    // locks
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private final MarketTechnicals marketTechnicals;

    @Inject
    public MarketData(MoneyPlantApplicationProperties moneyPlantApplicationProperties
            , MarketTechnicals marketTechnicals) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
        this.marketTechnicals = marketTechnicals;
        this.moneyPlantTickList = new ArrayList<>();
    }

    public void addTick(final MoneyPlantTick moneyPlantTick) {
        try {
            writeLock.lock();
            moneyPlantTickList.add(moneyPlantTick);
        } finally {
            writeLock.unlock();
        }
    }

    public MoneyPlantBar computeBar() {
        // 1. copy ticks
        List<MoneyPlantTick> lastBarTicks;
        try {
            readLock.lock();
            lastBarTicks = new ArrayList<>(moneyPlantTickList);
            log.debug("lastBarTickSize={}", lastBarTicks.size());
        } finally {
            readLock.unlock();
        }
        // 2. compute
        MoneyPlantBar moneyPlantBar = new MoneyPlantBar();
        // open
        moneyPlantBar.setOpenPrice(lastBarTicks.get(0).getLastTradedPrice());
        // close
        moneyPlantBar.setClosePrice(lastBarTicks.get(lastBarTicks.size() - 1).getLastTradedPrice());
        // low
        moneyPlantBar.setLowPrice(
                lastBarTicks.stream()
                        .mapToDouble(MoneyPlantTick::getLastTradedPrice)
                        .min()
                        .orElse(Double.NaN));
        // high
        moneyPlantBar.setHighPrice(
                lastBarTicks.stream()
                        .mapToDouble(MoneyPlantTick::getLastTradedPrice)
                        .max()
                        .orElse(Double.NaN));
        // volume
        moneyPlantBar.setVolume(lastBarTicks.stream()
                .mapToDouble(MoneyPlantTick::getTradeVolume)
                .average()
                .orElse(Double.NaN));
        // 3. clear ticks
        try {
            writeLock.lock();
            moneyPlantTickList.clear();
        } finally {
            writeLock.unlock();
        }
        return moneyPlantBar;
    }

    public void addBar(final MoneyPlantBar bar) {
        marketTechnicals.addBar(bar);
    }
}