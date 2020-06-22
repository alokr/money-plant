package dev.codethat.orangeplant.adapter;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import dev.codethat.moneyplant.core.adapter.SessionAdapter;
import dev.codethat.moneyplant.core.httpclient.MoneyPlantCoreHttpClient;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import dev.codethat.orangeplant.to.request.SessionRequestTO;
import dev.codethat.orangeplant.to.response.SessionResponseTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Objects;

@Named
@Slf4j
public class OrderAdapterImpl implements SessionAdapter<SessionRequestTO, SessionResponseTO> {
    private final OrangePlantApplicationProperties applicationProperties;

    @Inject
    public OrderAdapterImpl(OrangePlantApplicationProperties applicationProperties, MoneyPlantCoreHttpClient moneyPlantCoreHttpClient) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public SessionResponseTO login(SessionRequestTO sessionRequestTO) throws Exception {
        SessionResponseTO responseTO = null;
        KiteConnect kiteConnect = new KiteConnect(applicationProperties.getKiteConnect().getApi().getKey());
        kiteConnect.setUserId(applicationProperties.getKiteConnect().getUserId());
        if (applicationProperties.getKiteConnect().isLoggingEnabled()) {
            // TODO test
            KiteConnect.ENABLE_LOGGING = Boolean.TRUE;
        }
        // TODO refactor
        kiteConnect.setSessionExpiryHook(() -> {
            System.out.println("Session Expired");
        });
        try {
            User user = kiteConnect.generateSession(sessionRequestTO.getRequestToken(), applicationProperties.getKiteConnect().getApi().getSecret());
            if (!Objects.isNull(user)) {
                kiteConnect.setAccessToken(user.accessToken);
                kiteConnect.setPublicToken(user.publicToken);

                responseTO = new SessionResponseTO();
                responseTO.setKiteConnect(kiteConnect);
                responseTO.setUser(user);
            }
        } catch (KiteException | IOException e) {
            log.error(e.getMessage());
            throw new Exception();
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
