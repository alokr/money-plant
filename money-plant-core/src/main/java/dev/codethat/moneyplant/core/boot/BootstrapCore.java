package dev.codethat.moneyplant.core.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public interface BootstrapCore {
    boolean login(final BufferedReader reader) throws Exception;

    boolean margin(String segment) throws Exception;

    boolean instruments(String exchange) throws Exception;

    boolean quotes(String[] instruments) throws Exception;

    boolean streamData();

    boolean trade();
}
