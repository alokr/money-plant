package dev.codethat.orangeplant.runner;

import dev.codethat.moneyplant.core.runner.MoneyPlantCoreApplicationRunner;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import dev.codethat.orangeplant.boot.OrangePlantBootstrap;
import dev.codethat.orangeplant.constants.OrangePlantConstants;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Named
@Slf4j
public class OrangePlantApplicationRunner extends MoneyPlantCoreApplicationRunner {
    private final OrangePlantBootstrap bootstrap;

    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    private final OrangePlantApplicationProperties orangePlantApplicationProperties;

    @Inject
    public OrangePlantApplicationRunner(final OrangePlantBootstrap bootstrap
            , MoneyPlantApplicationProperties moneyPlantApplicationProperties
            , OrangePlantApplicationProperties orangePlantApplicationProperties) {
        this.bootstrap = bootstrap;
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
        this.orangePlantApplicationProperties = orangePlantApplicationProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Logging in...");
        try(final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            if (bootstrap.login(bufferedReader)) {
                log.info("Retrieving margin...");
                if (bootstrap.margin(OrangePlantConstants.MARGIN_SEGMENT)) {
                    log.info("Retrieving instruments...");
                    if (bootstrap.instruments(orangePlantApplicationProperties.getTradePreference().getExchange())) {
                        if (moneyPlantApplicationProperties.getMarketData().isTickerStreamingEnabled()) {
                            log.info("Initializing streaming...");
                            if (bootstrap.initStreaming()) {
                                log.info("Opening streaming...");
                                if (bootstrap.streamTicker()) {
                                    log.info("Ticker streaming started");
                                }
                            }
                        }
                        if (moneyPlantApplicationProperties.getMarketData().isTickerReadingEnabled()) {
                            log.info("Scheduling ticker reading...");
                            if (bootstrap.scheduleTickerReading(
                                    orangePlantApplicationProperties.getTradePreference().getExchange(),
                                    moneyPlantApplicationProperties.getMarketData().getCandlePeriod())) {
                            }
                        }
                    }
                }
            } else {
                log.error("Login failed");
            }
        }
    }
}
