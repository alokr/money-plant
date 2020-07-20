package dev.codethat.orangeplant.runner;

import dev.codethat.moneyplant.core.runner.MoneyPlantCoreApplicationRunner;
import dev.codethat.orangeplant.boot.OrangePlantBootstrap;
import dev.codethat.orangeplant.constants.OrangePlantConstants;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Named
@Slf4j
public class OrangePlantApplicationRunner extends MoneyPlantCoreApplicationRunner {
    private final OrangePlantBootstrap bootstrap;

    @Inject
    public OrangePlantApplicationRunner(final OrangePlantBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Logging in...");
        try(final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            if (bootstrap.login(bufferedReader)) {
                log.info("Retrieving margin...");
                if (bootstrap.margin(OrangePlantConstants.MARGIN_SEGMENT)) {
                    log.info("Retrieving instruments...");
                    if (bootstrap.instruments("NFO")) {
                        log.info("Initializing streaming...");
                        if (bootstrap.initStreaming()) {
                            log.info("Opening streaming...");
                            if (bootstrap.streamData()) {

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
