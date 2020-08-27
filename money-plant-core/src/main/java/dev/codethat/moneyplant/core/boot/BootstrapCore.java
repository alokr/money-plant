package dev.codethat.moneyplant.core.boot;

import java.io.BufferedReader;
import java.util.List;

public interface BootstrapCore {
    boolean login(final BufferedReader reader) throws Exception;

    boolean margin() throws Exception;

    boolean instruments() throws Exception;

    boolean initStreaming();

    boolean streamTicker();

    boolean scheduleTickerReading();

    boolean scheduleBarGenerator();

    <T> List<T> getInstrumentTokens();

    boolean trade();
}
