package dev.codethat.orangeplant.adapter;

import com.zerodhatech.ticker.*;
import dev.codethat.moneyplant.core.adapter.StreamingAdapter;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.moneyplant.core.listener.StreamingConnectionStatusCoreListener;
import dev.codethat.moneyplant.core.listener.StreamingOrderStatusCoreListener;
import dev.codethat.moneyplant.core.listener.StreamingTickCoreListener;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@Slf4j
public class StreamingAdapterImpl implements StreamingAdapter {
    private final MoneyPlantCache moneyPlantCache;
    private KiteTicker kiteTicker;
    private final StreamingConnectionStatusCoreListener connectionCoreListener;
    private final StreamingTickCoreListener tickCoreListener;
    private final StreamingOrderStatusCoreListener orderStatusCoreListener;

    @Inject
    public StreamingAdapterImpl(final MoneyPlantCache moneyPlantCache
            , final StreamingConnectionStatusCoreListener connectionCoreListener
            , final StreamingTickCoreListener tickCoreListener
            , final StreamingOrderStatusCoreListener orderStatusCoreListener) {
        this.moneyPlantCache = moneyPlantCache;
        this.connectionCoreListener = connectionCoreListener;
        this.tickCoreListener = tickCoreListener;
        this.orderStatusCoreListener = orderStatusCoreListener;
    }

    @Override
    public boolean init() {
        this.kiteTicker = moneyPlantCache.brokerWSClient();
        kiteTicker.setOnConnectedListener((OnConnect) connectionCoreListener);
        kiteTicker.setOnDisconnectedListener((OnDisconnect) connectionCoreListener);
        kiteTicker.setOnTickerArrivalListener((OnTicks) tickCoreListener);
        kiteTicker.setOnOrderUpdateListener((OnOrderUpdate) orderStatusCoreListener);
        return true;
    }

    @Override
    public boolean connect() {
        kiteTicker.connect();
        return true;
    }

    @Override
    public <T> boolean subscribe(List<T> tickerIds) {
        kiteTicker.subscribe((ArrayList<Long>) tickerIds);
        // TODO decide on the mode
        kiteTicker.setMode((ArrayList<Long>) tickerIds, KiteTicker.modeFull);
        return true;
    }

    @Override
    public <T> boolean unsubscribe(List<T> tickerIds) {
        kiteTicker.unsubscribe((ArrayList<Long>) tickerIds);
        return true;
    }

    @Override
    public boolean disconnect() {
        kiteTicker.disconnect();
        return true;
    }
}
