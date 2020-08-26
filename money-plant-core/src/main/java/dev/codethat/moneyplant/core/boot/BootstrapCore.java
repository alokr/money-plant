package dev.codethat.moneyplant.core.boot;

import java.io.BufferedReader;

public interface BootstrapCore {
    boolean login(final BufferedReader reader) throws Exception;

    boolean margin() throws Exception;

    boolean instruments() throws Exception;

    boolean initStreaming();

    boolean streamTicker();

    boolean scheduleTickerReading();

    boolean scheduleBarGenerator();

    boolean trade();
}
