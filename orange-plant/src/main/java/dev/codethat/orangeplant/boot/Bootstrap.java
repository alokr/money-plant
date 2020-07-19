package dev.codethat.orangeplant.boot;

import com.zerodhatech.models.Instrument;
import dev.codethat.moneyplant.core.boot.BootstrapCore;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.moneyplant.core.service.AccountService;
import dev.codethat.moneyplant.core.service.QuoteService;
import dev.codethat.orangeplant.service.SessionServiceImpl;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Named
@Slf4j
@AllArgsConstructor
public class Bootstrap implements BootstrapCore {
    private final static String SESSION_RESPONSE_TO_SER = "data/session/sessionResponseTO.ser";

    private final OrangePlantApplicationProperties properties;

    private final MoneyPlantCache moneyPlantCache;

    private final SessionServiceImpl sessionService;

    private final AccountService accountService;

    private final QuoteService quoteService;

    @Override
    public boolean login(final BufferedReader reader) throws Exception {
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
        log.info("Logged in");
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
        log.info("Retrieved margin");
        return true;
    }

    @Override
    public boolean instruments(String exchange) throws Exception {
        QuoteRequestTO quoteRequestTO = new QuoteRequestTO();
        quoteRequestTO.setExchange(exchange);
        QuoteResponseTO quoteResponseTO = (QuoteResponseTO) quoteService.instruments(quoteRequestTO);
        log.info("Retrieved instruments");
        extractTradingInstruments(quoteResponseTO.getInstruments());
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

    private void extractTradingInstruments(List<Instrument> instruments) {
        // TODO stocks
        Map<String, List<Instrument>> tradingInstruments = instruments.stream().filter(instrument
                -> instrument.exchange.equals(properties.getTradePreference().getExchange())
                && properties.getTradePreference().getSegments().contains(instrument.segment)
                && instrument.name.equals(properties.getTradePreference().getInstrument())
                && instrument.expiry.getTime() >= properties.getTradePreference().getExpiryStartDate().getTime()
                && instrument.expiry.getTime() <= properties.getTradePreference().getExpiryEndDate().getTime()
        ).collect(Collectors.groupingBy(Instrument::getSegment));
        moneyPlantCache.CACHE
                .put(MoneyPlantCache.TRADING_STOCKS
                        , tradingInstruments.get(properties.getTradePreference().getSegments().get(0)));
        moneyPlantCache.CACHE
                .put(MoneyPlantCache.TRADING_FUTURE_INSTRUMENTS
                        , tradingInstruments.get(properties.getTradePreference().getSegments().get(1)));
        moneyPlantCache.CACHE
                .put(MoneyPlantCache.TRADING_OPTION_INSTRUMENTS
                        , tradingInstruments.get(properties.getTradePreference().getSegments().get(2)));
        log.info("Extracted trading instruments");
    }
}
