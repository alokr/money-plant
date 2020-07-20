package dev.codethat.moneyplant.core.cache;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
public abstract class MoneyPlantCache {
    public static String BROKER_HTTP_CLIENT_KEY = "broker-http-client-session";
    public static String BROKER_WS_CLIENT_KEY = "broker-ws-client-session";
    public static String USER_SESSION_KEY = "user-session";
    public static String TRADING_STOCKS = "stocks";
    public static String FUTURE_INSTRUMENTS = "future-instruments";
    public static String OPTION_INSTRUMENTS = "option-instruments";
    public static String TRADING_FUTURE_INSTRUMENTS = "trading-future-instruments";
    public static String TRADING_OPTION_INSTRUMENTS = "trading-option-instruments";

    public Map<String, Object> CACHE = new HashMap<>();

    public abstract <T> T httpBrokerClient();

    public abstract <T> T wsBrokerClient();

    public abstract <T> T user();
}
