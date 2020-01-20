package dev.codethat.orangeplant.adapter;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import dev.codethat.moneyplant.core.adapter.SessionAdapter;
import dev.codethat.moneyplant.core.httpclient.MoneyPlantCoreHttpClient;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import dev.codethat.orangeplant.to.request.SessionRequestTO;
import dev.codethat.orangeplant.to.response.SessionResponseTO;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
public class SessionAdapterImpl implements SessionAdapter<SessionRequestTO, SessionResponseTO> {
    private final OrangePlantApplicationProperties applicationProperties;
    private final MoneyPlantCoreHttpClient moneyPlantCoreHttpClient;

    @Inject
    public SessionAdapterImpl(OrangePlantApplicationProperties applicationProperties, MoneyPlantCoreHttpClient moneyPlantCoreHttpClient) {
        this.applicationProperties = applicationProperties;
        this.moneyPlantCoreHttpClient = moneyPlantCoreHttpClient;
    }

    @Override
    public SessionResponseTO login(SessionRequestTO sessionRequestTO) {
        SessionResponseTO responseTO = null;
        KiteConnect kiteConnect = new KiteConnect(applicationProperties.getKiteConnect().getApi().getKey());
        kiteConnect.setUserId(applicationProperties.getKiteConnect().getUserId());
        try {
            User user = kiteConnect.generateSession(sessionRequestTO.getRequestToken(), applicationProperties.getKiteConnect().getApi().getSecret());
            if (user != null) {
                kiteConnect.setAccessToken(user.accessToken);
                kiteConnect.setPublicToken(user.publicToken);

                responseTO = new SessionResponseTO();
                responseTO.setKiteConnect(kiteConnect);
                responseTO.setUser(user);
            }
        } catch (KiteException | IOException e){
            e.printStackTrace();
        }
        return responseTO;
    }

    @Override
    public SessionResponseTO refresh(SessionRequestTO sessionRequestTO) {
        return null;
    }

    @Override
    public SessionResponseTO logout(SessionRequestTO sessionRequestTO) {
        return null;
    }
}
