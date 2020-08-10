package dev.codethat.orangeplant.cache;

import dev.codethat.moneyplant.core.cache.MoneyPlantCache;

import javax.inject.Named;

@Named
public class OrangePlantCache extends MoneyPlantCache {
    @Override
    public <T> T brokerHttpClient() {
        return (T) CACHE.get(MoneyPlantCache.BROKER_HTTP_CLIENT_KEY);
    }

    @Override
    public <T> T brokerWSClient() {
        return (T) CACHE.get(MoneyPlantCache.BROKER_WS_CLIENT_KEY);
    }

    @Override
    public <T> T user() {
        return (T) CACHE.get(MoneyPlantCache.USER_SESSION_KEY);
    }
}
