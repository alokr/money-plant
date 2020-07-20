package dev.codethat.moneyplant.core.boot;

import java.io.BufferedReader;

public interface BootstrapCore {
    boolean login(final BufferedReader reader) throws Exception;

    boolean margin(String segment) throws Exception;

    boolean instruments(String exchange) throws Exception;

    boolean initStreaming();

    boolean streamData();

    boolean trade();
}
