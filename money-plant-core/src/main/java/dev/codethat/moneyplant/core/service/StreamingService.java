package dev.codethat.moneyplant.core.service;

import java.util.List;

public interface StreamingService {
    void init();

    void connect();

    <T> void subscribe(List<T> tickerIds);

    <T> void unsubscribe(List<T> tickerIds);

    void disconnect();
}
