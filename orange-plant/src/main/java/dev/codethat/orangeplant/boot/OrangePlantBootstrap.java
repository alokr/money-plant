package dev.codethat.orangeplant.boot;

import com.zerodhatech.models.Instrument;
import dev.codethat.moneyplant.core.boot.BootstrapCore;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.moneyplant.core.constants.MoneyPlantConstants;
import dev.codethat.moneyplant.core.service.*;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import dev.codethat.moneyplant.core.util.FileUtil;
import dev.codethat.orangeplant.bean.request.AccountRequestTO;
import dev.codethat.orangeplant.bean.request.QuoteRequestTO;
import dev.codethat.orangeplant.bean.request.SessionRequestTO;
import dev.codethat.orangeplant.bean.response.AccountResponseTO;
import dev.codethat.orangeplant.bean.response.QuoteResponseTO;
import dev.codethat.orangeplant.bean.response.SessionResponseTO;
import dev.codethat.orangeplant.constants.OrangePlantConstants;
import dev.codethat.orangeplant.scheduler.OrangePlantReadCandleTask;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import dev.codethat.orangeplant.util.DataExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Named
@Slf4j
@AllArgsConstructor
public class OrangePlantBootstrap implements BootstrapCore {
    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    private final OrangePlantApplicationProperties orangePlantApplicationProperties;

    private final MoneyPlantCache moneyPlantCache;

    private final SessionService sessionService;

    private final AccountService accountService;

    private final QuoteService quoteService;

    private final OrderService orderService;

    private final StreamingService streamingService;

    private final FileUtil fileUtil;

    private final DataExtractor dataExtractor;

    @Inject
    @Qualifier(MoneyPlantConstants.BEAN_READ_CANDLE_TASK_SCHEDULER)
    private final TaskScheduler readCandleTaskScheduler;

    private final OrangePlantReadCandleTask readCandleTask;

    @Override
    public boolean login(final BufferedReader reader) throws Exception {
        SessionResponseTO sessionResponseTO = (SessionResponseTO) fileUtil.deserializeSession(OrangePlantConstants.SESSION_RESPONSE_TO_SER);
        if (Objects.isNull(sessionResponseTO)) {
            log.info("Enter requestToken:");
            String requestToken = reader.readLine();
            if (Objects.isNull(requestToken) || requestToken.equals("0")) {
                throw new IllegalArgumentException("requestToken is missing!");
            }
            SessionRequestTO sessionRequestTO = new SessionRequestTO();
            sessionRequestTO.setRequestToken(requestToken);
            log.info("Creating new session");
            sessionResponseTO = (SessionResponseTO) sessionService.login(sessionRequestTO);
            if (!Objects.isNull(sessionResponseTO)) {
                fileUtil.serializeSession(sessionResponseTO, OrangePlantConstants.SESSION_RESPONSE_TO_SER);
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
        dataExtractor.extractInstruments(quoteResponseTO.getInstruments());
        return true;
    }

    @Override
    public boolean initStreaming() {
        streamingService.init();
        log.info("Initialized streaming");
        return true;
    }

    @Override
    public boolean streamTicker() {
        streamingService.connect();
        List<Long> instrumentTokens = ((Map<String, Instrument>) moneyPlantCache.CACHE
                .get(MoneyPlantCache.TRADING_FUTURE_INSTRUMENTS))
                .values()
                .stream()
                .map(Instrument::getInstrument_token)
                .collect(Collectors.toList());
        log.info("Subscribing instruments...");
        streamingService.subscribe(instrumentTokens);
        log.info("Subscribed instruments");
        return true;
    }

    @Override
    public boolean scheduleTickerReading(String exchange, long candlePeriod) {
        List<String> instrumentTokens = ((Map<String, Instrument>) moneyPlantCache.CACHE
                .get(MoneyPlantCache.TRADING_FUTURE_INSTRUMENTS))
                .values()
                .stream()
                .map(Instrument::getInstrument_token)
                .map(String::valueOf)
                .collect(Collectors.toList());

        QuoteRequestTO quoteRequestTO = new QuoteRequestTO();
        quoteRequestTO.setExchange(exchange);
        quoteRequestTO.setInstruments(instrumentTokens.toArray(new String[] {}));
        readCandleTask.setQuoteRequestTO(quoteRequestTO);

        readCandleTaskScheduler.scheduleWithFixedDelay(
                readCandleTask
                , Instant.now()
                , Duration.ofMillis(candlePeriod));
        log.info("Ticker reading scheduled");
        return true;
    }

    @Override
    public boolean trade() {
        return false;
    }
}
