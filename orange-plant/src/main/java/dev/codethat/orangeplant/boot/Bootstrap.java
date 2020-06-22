package dev.codethat.orangeplant.boot;

import dev.codethat.moneyplant.core.boot.BootstrapCore;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.moneyplant.core.service.AccountService;
import dev.codethat.moneyplant.core.service.QuoteService;
import dev.codethat.orangeplant.service.SessionServiceImpl;
import dev.codethat.orangeplant.to.request.AccountRequestTO;
import dev.codethat.orangeplant.to.request.QuoteRequestTO;
import dev.codethat.orangeplant.to.request.SessionRequestTO;
import dev.codethat.orangeplant.to.response.AccountResponseTO;
import dev.codethat.orangeplant.to.response.QuoteResponseTO;
import dev.codethat.orangeplant.to.response.SessionResponseTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

@Named
@Slf4j
@AllArgsConstructor
public class Bootstrap implements BootstrapCore {
    private final static String SESSION_RESPONSE_TO_SER = "data/session/sessionResponseTO.ser";

    private final MoneyPlantCache moneyPlantCache;

    private final SessionServiceImpl sessionService;

    private final AccountService accountService;

    private final QuoteService quoteService;

    @Override
    public boolean login(final BufferedReader reader) throws Exception {
        log.info("Logging in...");
        SessionResponseTO sessionResponseTO = null;

        File file = new File(SESSION_RESPONSE_TO_SER);
        if (file.exists()) {
            log.info("Retrieving session");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(SESSION_RESPONSE_TO_SER)));
            sessionResponseTO = (SessionResponseTO) ois.readObject();
        }
        if (sessionResponseTO == null) {
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
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(SESSION_RESPONSE_TO_SER)));
                oos.writeObject(sessionResponseTO);
                log.info("Persisted session");
            } else {
                return false;
            }
        }
        moneyPlantCache.CACHE.put(MoneyPlantCache.BROKER_CLIENT_SESSION_KEY, sessionResponseTO.getKiteConnect());
        moneyPlantCache.CACHE.put(MoneyPlantCache.USER_SESSION_KEY, sessionResponseTO.getUser());
        log.info("Cached session");
        return true;
    }

    @Override
    public boolean margin(String segment) throws Exception {
        AccountRequestTO accountRequestTO = new AccountRequestTO();
        accountRequestTO.setSegment(segment);

        AccountResponseTO accountResponseTO = (AccountResponseTO) accountService.margin(accountRequestTO);
        log.info("Net={}", accountResponseTO.getMargin().net);
        log.info("Cash={}", accountResponseTO.getMargin().available.cash);
        log.info("OptionPremium={}", accountResponseTO.getMargin().utilised.optionPremium);
        log.info("Margin retrieved");
        return true;
    }

    @Override
    public boolean instruments(String exchange) throws Exception {
        QuoteRequestTO quoteRequestTO = new QuoteRequestTO();
        quoteRequestTO.setExchange(exchange);
        QuoteResponseTO quoteResponseTO = (QuoteResponseTO) quoteService.instruments(quoteRequestTO);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JUNE, 27);
        quoteResponseTO.getInstruments().stream().filter(instrument
                -> instrument.tradingsymbol.startsWith("BANKNIFTY")
                && (instrument.segment.equals("NFO-OPT"))
                && instrument.expiry.getTime() < calendar.getTimeInMillis()
        ).forEach(instrument -> {
            log.info(instrument.toString());
        });
        log.info("Instruments retrieved");
        return true;
    }

    @Override
    public boolean quotes(String[] instruments) throws Exception {
        return false;
    }

    @Override
    public boolean streamData() {
        return false;
    }

    @Override
    public boolean trade() {
        return false;
    }
}
