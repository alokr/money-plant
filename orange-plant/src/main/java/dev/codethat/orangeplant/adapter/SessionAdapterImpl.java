package dev.codethat.orangeplant.adapter;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import com.zerodhatech.ticker.KiteTicker;
import dev.codethat.moneyplant.core.adapter.SessionAdapter;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import dev.codethat.orangeplant.bean.request.SessionRequestTO;
import dev.codethat.orangeplant.bean.response.SessionResponseTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Objects;

@Named
@Slf4j
public class SessionAdapterImpl implements SessionAdapter<SessionRequestTO, SessionResponseTO> {
    private final OrangePlantApplicationProperties properties;

    @Inject
    public SessionAdapterImpl(OrangePlantApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public SessionResponseTO login(SessionRequestTO sessionRequestTO) throws Exception {
        SessionResponseTO responseTO = null;
        // http client
        KiteConnect kiteConnect = new KiteConnect(properties.getKiteConnect().getApi().getKey());
        kiteConnect.setUserId(properties.getKiteConnect().getUserId());
        if (properties.getKiteConnect().isLoggingEnabled()) {
            // TODO test
            KiteConnect.ENABLE_LOGGING = Boolean.TRUE;
        }
        // TODO refresh session
        kiteConnect.setSessionExpiryHook(() -> {
            log.warn("Session Expired");
        });
        try {
            User user = kiteConnect.generateSession(sessionRequestTO.getRequestToken()
                    , properties.getKiteConnect().getApi().getSecret());
            if (!Objects.isNull(user)) {
                kiteConnect.setAccessToken(user.accessToken);
                kiteConnect.setPublicToken(user.publicToken);

                responseTO = new SessionResponseTO();
                responseTO.setKiteConnect(kiteConnect);
                // ws client
                responseTO.setKiteTicker(
                        new KiteTicker(kiteConnect.getAccessToken(), properties.getKiteConnect().getApi().getKey()));
                responseTO.setUser(user);
            }
        } catch (KiteException | IOException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
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
