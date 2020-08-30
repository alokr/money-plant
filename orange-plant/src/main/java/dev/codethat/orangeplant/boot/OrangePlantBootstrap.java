package dev.codethat.orangeplant.boot;

import com.zerodhatech.models.Instrument;
import dev.codethat.moneyplant.core.bean.request.OrderRequestCoreTO;
import dev.codethat.moneyplant.core.bean.response.OrderResponseCoreTO;
import dev.codethat.moneyplant.core.boot.BootstrapCore;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.moneyplant.core.constants.MoneyPlantConstants;
import dev.codethat.moneyplant.core.service.*;
import dev.codethat.moneyplant.core.simulator.TickSimulatorTask;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import dev.codethat.moneyplant.core.task.BarGeneratorTask;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;

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
public class OrangePlantBootstrap implements BootstrapCore {
    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    private final OrangePlantApplicationProperties orangePlantApplicationProperties;

    private final MoneyPlantCache moneyPlantCache;

    private final SessionService<SessionRequestTO, ? extends SessionResponseTO> sessionService;

    private final AccountService<AccountRequestTO, ? extends AccountResponseTO> accountService;

    private final QuoteService<QuoteRequestTO, ? extends QuoteResponseTO> quoteService;

    private final OrderService<? extends dev.codethat.moneyplant.core.bean.request.OrderRequestCoreTO, ? extends dev.codethat.moneyplant.core.bean.response.OrderResponseCoreTO> orderService;

    private final StreamingService streamingService;

    private final FileUtil fileUtil;

    private final DataExtractor dataExtractor;

    private final TaskScheduler readCandleTaskScheduler;

    private final TaskScheduler barGeneratorTaskScheduler;

    private final TaskScheduler marketSimulationTaskScheduler;

    private final OrangePlantReadCandleTask readCandleTask;

    private final BarGeneratorTask barGeneratorTask;

    private final TickSimulatorTask tickSimulatorTask;

    public OrangePlantBootstrap(MoneyPlantApplicationProperties moneyPlantApplicationProperties
            , OrangePlantApplicationProperties orangePlantApplicationProperties
            , MoneyPlantCache moneyPlantCache, SessionService<SessionRequestTO, ? extends SessionResponseTO> sessionService
            , AccountService<AccountRequestTO, ? extends AccountResponseTO> accountService, QuoteService<QuoteRequestTO, ? extends QuoteResponseTO> quoteService
            , OrderService<? extends OrderRequestCoreTO, ? extends OrderResponseCoreTO> orderService, StreamingService streamingService
            , FileUtil fileUtil, DataExtractor dataExtractor
            , @Qualifier(MoneyPlantConstants.BEAN_CANDLE_READING_TASK_SCHEDULER) TaskScheduler readCandleTaskScheduler
            , @Qualifier(MoneyPlantConstants.BEAN_BAR_GENERATION_TASK_SCHEDULER) TaskScheduler barGeneratorTaskScheduler
            , @Qualifier(MoneyPlantConstants.BEAN_MARKET_SIMULATION_TASK_SCHEDULER)  TaskScheduler marketSimulationTaskScheduler
            , OrangePlantReadCandleTask readCandleTask
            , BarGeneratorTask barGeneratorTask
            , TickSimulatorTask tickSimulatorTask) {
        this.moneyPlantApplicationProperties = moneyPlantApplicationProperties;
        this.orangePlantApplicationProperties = orangePlantApplicationProperties;
        this.moneyPlantCache = moneyPlantCache;
        this.sessionService = sessionService;
        this.accountService = accountService;
        this.quoteService = quoteService;
        this.orderService = orderService;
        this.streamingService = streamingService;
        this.fileUtil = fileUtil;
        this.dataExtractor = dataExtractor;
        this.readCandleTaskScheduler = readCandleTaskScheduler;
        this.barGeneratorTaskScheduler = barGeneratorTaskScheduler;
        this.marketSimulationTaskScheduler = marketSimulationTaskScheduler;
        this.readCandleTask = readCandleTask;
        this.barGeneratorTask = barGeneratorTask;
        this.tickSimulatorTask = tickSimulatorTask;
    }

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
            sessionResponseTO = sessionService.login(sessionRequestTO);
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
    public boolean margin() throws Exception {
        AccountRequestTO accountRequestTO = new AccountRequestTO();
        accountRequestTO.setSegment(orangePlantApplicationProperties.getKiteConnect().getAccount().getSegment());

        AccountResponseTO accountResponseTO = accountService.margin(accountRequestTO);
        log.info("Net={}", accountResponseTO.getMargin().net);
        log.info("Cash={}", accountResponseTO.getMargin().available.cash);
        log.info("OptionPremium={}", accountResponseTO.getMargin().utilised.optionPremium);
        log.info("Retrieved margin");
        return true;
    }

    @Override
    public boolean instruments() throws Exception {
        QuoteRequestTO quoteRequestTO = new QuoteRequestTO();
        quoteRequestTO.setExchange(orangePlantApplicationProperties.getTradePreference().getExchange());
        QuoteResponseTO quoteResponseTO = quoteService.instruments(quoteRequestTO);
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
        log.info("Subscribing instruments...");
        streamingService.subscribe(getInstrumentTokens());
        log.info("Subscribed instruments");
        return true;
    }

    @Override
    public boolean scheduleTickerReading() {
        QuoteRequestTO quoteRequestTO = new QuoteRequestTO();
        quoteRequestTO.setExchange(orangePlantApplicationProperties.getTradePreference().getExchange());
        quoteRequestTO.setInstruments(getInstrumentTokens().toArray(new String[] {}));
        readCandleTask.setQuoteRequestTO(quoteRequestTO);

        readCandleTaskScheduler.scheduleWithFixedDelay(
                readCandleTask
                , Instant.now()
                , Duration.ofMillis(moneyPlantApplicationProperties.getMarketData().getTickerPeriod()));
        log.info("Ticker reading scheduled");
        return true;
    }

    @Override
    public boolean scheduleBarGenerator() {
        barGeneratorTaskScheduler.scheduleWithFixedDelay(
                barGeneratorTask
                , Instant.now()
                , Duration.ofMillis(moneyPlantApplicationProperties.getMarketData().getCandlePeriod()));
        log.info("Bar generator scheduled");
        return true;
    }

    @Override
    public boolean simulateMarketData() {
        barGeneratorTaskScheduler.scheduleWithFixedDelay(
                barGeneratorTask
                , Instant.now()
                , Duration.ofMillis(moneyPlantApplicationProperties.getMarketData().getCandlePeriod()));
        log.info("Bar generator scheduled");
        return true;
    }

    @Override
    public boolean trade() {
        return false;
    }

    public <T> List<T> getInstrumentTokens() {
        List<Long> list = null;
        if (orangePlantApplicationProperties.getTradePreference().getExchange()
                .equals(orangePlantApplicationProperties.getMarket().getEqExchange())) {
            list = ((List<Instrument>) moneyPlantCache.CACHE.get(MoneyPlantCache.TRADING_STOCKS))
                    .stream()
                    .map(Instrument::getInstrument_token)
                    .collect(Collectors.toList());
        }
        if (orangePlantApplicationProperties.getTradePreference().getExchange()
                .equals(orangePlantApplicationProperties.getMarket().getFutOptExchange())) {
            list = ((Map<String, Instrument>) moneyPlantCache.CACHE.get(MoneyPlantCache.TRADING_FUTURE_INSTRUMENTS))
                    .values()
                    .stream()
                    .map(Instrument::getInstrument_token)
                    .collect(Collectors.toList());
        }
        return (List<T>) list;
    }
}
