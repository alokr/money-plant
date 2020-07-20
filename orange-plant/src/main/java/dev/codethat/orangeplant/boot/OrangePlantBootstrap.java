package dev.codethat.orangeplant.boot;

import com.zerodhatech.models.Instrument;
import dev.codethat.moneyplant.core.boot.BootstrapCore;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.moneyplant.core.service.AccountService;
import dev.codethat.moneyplant.core.service.QuoteService;
import dev.codethat.moneyplant.core.service.SessionService;
import dev.codethat.moneyplant.core.service.StreamingService;
import dev.codethat.moneyplant.core.util.FileUtil;
import dev.codethat.orangeplant.to.request.AccountRequestTO;
import dev.codethat.orangeplant.to.request.QuoteRequestTO;
import dev.codethat.orangeplant.to.request.SessionRequestTO;
import dev.codethat.orangeplant.to.response.AccountResponseTO;
import dev.codethat.orangeplant.to.response.QuoteResponseTO;
import dev.codethat.orangeplant.to.response.SessionResponseTO;
import dev.codethat.orangeplant.util.DataExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Named
@Slf4j
@AllArgsConstructor
public class OrangePlantBootstrap implements BootstrapCore {
    private final static String SESSION_RESPONSE_TO_SER = "data/orange/session/sessionResponseTO.ser";

    private final MoneyPlantCache moneyPlantCache;

    private final SessionService<SessionRequestTO, SessionResponseTO> sessionService;

    private final AccountService<AccountRequestTO, AccountResponseTO> accountService;

    private final QuoteService<QuoteRequestTO, QuoteResponseTO> quoteService;

    private final StreamingService streamingService;

    private final FileUtil fileUtil;

    private final DataExtractor beanUtil;

    @Override
    public boolean login(final BufferedReader reader) throws Exception {
        SessionResponseTO sessionResponseTO = (SessionResponseTO) fileUtil.serializeSession(SESSION_RESPONSE_TO_SER);
        if (Objects.isNull(sessionResponseTO)) {
            log.info("Enter requestToken:");
            String requestToken = reader.readLine();
            if (Objects.isNull(requestToken) || requestToken.equals("0")) {
                throw new IllegalArgumentException("requestToken is missing!");
            }
            SessionRequestTO sessionRequestTO = new SessionRequestTO();
            sessionRequestTO.setRequestToken(requestToken);
            log.info("Creating new session");
            sessionResponseTO = sessionService.login(sessionRequestTO);
            if (!Objects.isNull(sessionResponseTO)) {
                fileUtil.deserializeSession(sessionResponseTO, SESSION_RESPONSE_TO_SER);
            } else {
                return false;
            }
        }
        // TODO move this logic to service?
        moneyPlantCache.CACHE.put(MoneyPlantCache.BROKER_HTTP_CLIENT_KEY, sessionResponseTO.getKiteConnect());
        moneyPlantCache.CACHE.put(MoneyPlantCache.BROKER_WS_CLIENT_KEY, sessionResponseTO.getKiteTicker());
        moneyPlantCache.CACHE.put(MoneyPlantCache.USER_SESSION_KEY, sessionResponseTO.getUser());
        log.info("Cached sessions");
        log.info("Logged in");
        return true;
    }

    @Override
    public boolean margin(String segment) throws Exception {
        AccountRequestTO accountRequestTO = new AccountRequestTO();
        accountRequestTO.setSegment(segment);

        AccountResponseTO accountResponseTO = accountService.margin(accountRequestTO);
        log.info("Net={}", accountResponseTO.getMargin().net);
        log.info("Cash={}", accountResponseTO.getMargin().available.cash);
        log.info("OptionPremium={}", accountResponseTO.getMargin().utilised.optionPremium);
        log.info("Retrieved margin");
        return true;
    }

    @Override
    public boolean instruments(String exchange) throws Exception {
        QuoteRequestTO quoteRequestTO = new QuoteRequestTO();
        quoteRequestTO.setExchange(exchange);
        QuoteResponseTO quoteResponseTO = quoteService.instruments(quoteRequestTO);
        log.info("Retrieved instruments");
        beanUtil.extractInstruments(quoteResponseTO.getInstruments());
        return true;
    }

    @Override
    public boolean initStreaming() {
        streamingService.init();
        log.info("Initialized streaming");
        return true;
    }

    @Override
    public boolean streamData() {
        streamingService.connect();
        List<Long> tickerIds = ((Map<String, Instrument>) moneyPlantCache.CACHE
                .get(MoneyPlantCache.TRADING_FUTURE_INSTRUMENTS))
                .values()
                .stream()
                .map(Instrument::getInstrument_token)
                .collect(Collectors.toList());
        log.info("Subscribing instruments...");
        streamingService.subscribe(tickerIds);
        log.info("Subscribed instruments...");
        return true;
    }

    @Override
    public boolean trade() {
        return false;
    }
}
