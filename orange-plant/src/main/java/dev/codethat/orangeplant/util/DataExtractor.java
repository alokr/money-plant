package dev.codethat.orangeplant.util;

import com.zerodhatech.models.Instrument;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Named
@AllArgsConstructor
@Slf4j
public class DataExtractor {
    private final OrangePlantApplicationProperties properties;

    private final MoneyPlantCache moneyPlantCache;

    public void extractInstruments(List<Instrument> instruments) {
        // instruments
        if (properties.getTradePreference().getExchange().equals(properties.getMarket().getEqExchange())) {
            Map<String, List<Instrument>> instrumentMap = instruments.stream().filter(instrument
                    -> instrument.exchange.equals(properties.getTradePreference().getExchange())
                    && properties.getTradePreference().getSegments().contains(instrument.segment)
            ).collect(Collectors.groupingBy(Instrument::getSegment));
            log.info("Extracted instruments");
            moneyPlantCache.CACHE
                    .put(MoneyPlantCache.TRADING_STOCKS
                            , instrumentMap.get(properties.getTradePreference().getSegments().get(0)));
            log.info("Extracted trading instruments");
        }
        if (properties.getTradePreference().getExchange().equals(properties.getMarket().getFutOptExchange())) {
            Map<String, List<Instrument>> instrumentMap = instruments.stream().filter(instrument
                    -> instrument.exchange.equals(properties.getTradePreference().getExchange())
                    && properties.getTradePreference().getSegments().contains(instrument.segment)
                    && instrument.expiry.getTime() >= properties.getTradePreference().getExpiryStartDate().getTime()
                    && instrument.expiry.getTime() <= properties.getTradePreference().getExpiryEndDate().getTime()
            ).collect(Collectors.groupingBy(Instrument::getSegment));
            moneyPlantCache.CACHE
                    .put(MoneyPlantCache.FUTURE_INSTRUMENTS
                            , instrumentMap.get(properties.getTradePreference().getSegments().get(1)));
            moneyPlantCache.CACHE
                    .put(MoneyPlantCache.OPTION_INSTRUMENTS
                            , instrumentMap.get(properties.getTradePreference().getSegments().get(2)));
            log.info("Extracted instruments");
            // trading instruments
            Map<String, Instrument> tradingFutureMap
                    = ((List<Instrument>) moneyPlantCache.CACHE.get(MoneyPlantCache.FUTURE_INSTRUMENTS))
                    .stream()
                    .filter(instrument
                            -> instrument.exchange.equals(properties.getTradePreference().getExchange())
                            && instrument.name.equals(properties.getTradePreference().getInstrument())
                            && properties.getTradePreference().getSegments().contains(instrument.segment)
                            && instrument.expiry.getTime() >= properties.getTradePreference().getExpiryStartDate().getTime()
                            && instrument.expiry.getTime() <= properties.getTradePreference().getExpiryEndDate().getTime()
                    ).collect(Collectors.toMap(Instrument::getTradingsymbol, Function.identity()));
            moneyPlantCache.CACHE
                    .put(MoneyPlantCache.TRADING_FUTURE_INSTRUMENTS, tradingFutureMap);
            Map<String, Instrument> tradingOptionMap
                    = ((List<Instrument>) moneyPlantCache.CACHE.get(MoneyPlantCache.OPTION_INSTRUMENTS))
                    .stream()
                    .filter(instrument
                            -> instrument.exchange.equals(properties.getTradePreference().getExchange())
                            && instrument.name.equals(properties.getTradePreference().getInstrument())
                            && properties.getTradePreference().getSegments().contains(instrument.segment)
                            && instrument.expiry.getTime() >= properties.getTradePreference().getExpiryStartDate().getTime()
                            && instrument.expiry.getTime() <= properties.getTradePreference().getExpiryEndDate().getTime()
                    ).collect(Collectors.toMap(Instrument::getStrike, Function.identity(), (o1, o2) -> o1, TreeMap::new));
            moneyPlantCache.CACHE
                    .put(MoneyPlantCache.TRADING_OPTION_INSTRUMENTS, tradingOptionMap);
            log.info("Extracted trading instruments");
        }
    }
}
