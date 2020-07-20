package dev.codethat.moneyplant.core.adapter;

import java.util.List;

public interface StreamingAdapter {
    boolean init();

    boolean connect();

    <T> boolean subscribe(List<T> tickerIds);

    <T> boolean unsubscribe(List<T> tickerIds);

    boolean disconnect();
}
