package dev.codethat.orangeplant.adapter;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import dev.codethat.moneyplant.core.adapter.AccountAdapter;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.moneyplant.core.httpclient.MoneyPlantCoreHttpClient;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import dev.codethat.orangeplant.bean.request.AccountRequestTO;
import dev.codethat.orangeplant.bean.response.AccountResponseTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
@Slf4j
public class AccountAdapterImpl implements AccountAdapter<AccountRequestTO, AccountResponseTO> {
    private final OrangePlantApplicationProperties applicationProperties;

    private final MoneyPlantCache moneyPlantCache;

    @Inject
    public AccountAdapterImpl(OrangePlantApplicationProperties applicationProperties, MoneyPlantCache moneyPlantCache
            , MoneyPlantCoreHttpClient moneyPlantCoreHttpClient) {
        this.applicationProperties = applicationProperties;
        this.moneyPlantCache = moneyPlantCache;
    }

    @Override
    public AccountResponseTO margin(AccountRequestTO accountRequestTO) throws Exception {
        KiteConnect kiteConnect = moneyPlantCache.brokerHttpClient();
        AccountResponseTO accountResponseTO = new AccountResponseTO();
        try {
            accountResponseTO.setMargin(kiteConnect.getMargins(accountRequestTO.getSegment()));
        } catch (KiteException | IOException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
            throw new Exception();
        }
        return accountResponseTO;
    }
}
