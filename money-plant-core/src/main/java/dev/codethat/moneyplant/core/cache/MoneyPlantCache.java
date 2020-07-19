package dev.codethat.moneyplant.core.cache;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
public abstract class MoneyPlantCache {
    public static String BROKER_CLIENT_SESSION_KEY = "broker-client-session";
    public static String USER_SESSION_KEY = "user-session";
    public static String TRADING_STOCKS = "stocks";
    public static String TRADING_FUTURE_INSTRUMENTS = "future-instruments";
    public static String TRADING_OPTION_INSTRUMENTS = "option-instruments";

    public Map<String, Object> CACHE = new HashMap<>();

    public abstract <T> T broker();

    public abstract <T> T user();
}
