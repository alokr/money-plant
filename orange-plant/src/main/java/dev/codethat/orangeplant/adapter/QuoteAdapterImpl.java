package dev.codethat.orangeplant.adapter;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import dev.codethat.moneyplant.core.adapter.QuoteAdapter;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.orangeplant.bean.request.QuoteRequestTO;
import dev.codethat.orangeplant.bean.response.QuoteResponseTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Named
@Slf4j
@AllArgsConstructor
public class QuoteAdapterImpl implements QuoteAdapter<QuoteRequestTO, QuoteResponseTO> {
    private final MoneyPlantCache moneyPlantCache;

    @Override
    public QuoteResponseTO instruments(QuoteRequestTO quoteRequestTO) throws Exception {
        KiteConnect kiteConnect = moneyPlantCache.brokerHttpClient();
        QuoteResponseTO quoteResponseTO;
        try {
            quoteResponseTO = new QuoteResponseTO();
            quoteResponseTO.setInstruments(kiteConnect.getInstruments(quoteRequestTO.getExchange()));
        } catch (KiteException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
            throw new Exception(e);
        }
        return quoteResponseTO;
    }

    @Override
    public QuoteResponseTO quote(QuoteRequestTO quoteRequestTO) throws Exception {
        KiteConnect kiteConnect = moneyPlantCache.brokerHttpClient();
        QuoteResponseTO quoteResponseTO;
        try {
            quoteResponseTO = new QuoteResponseTO();
            quoteResponseTO.setQuoteMap(kiteConnect.getQuote(quoteRequestTO.getInstruments()));
        } catch (KiteException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
            throw new Exception(e);
        }
        return quoteResponseTO;
    }

    @Override
    public QuoteResponseTO ohlcQuote(QuoteRequestTO quoteRequestTO) throws Exception {
        KiteConnect kiteConnect = moneyPlantCache.brokerHttpClient();
        QuoteResponseTO quoteResponseTO;
        try {
            quoteResponseTO = new QuoteResponseTO();
            quoteResponseTO.setOhlcQuoteMapMap(kiteConnect.getOHLC(quoteRequestTO.getInstruments()));
        } catch (KiteException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
            throw new Exception(e);
        }
        return quoteResponseTO;
    }

    @Override
    public QuoteResponseTO ltp(QuoteRequestTO quoteRequestTO) throws Exception {
        KiteConnect kiteConnect = moneyPlantCache.brokerHttpClient();
        QuoteResponseTO quoteResponseTO;
        try {
            quoteResponseTO = new QuoteResponseTO();
            quoteResponseTO.setLtpQuoteMap(kiteConnect.getLTP(quoteRequestTO.getInstruments()));
        } catch (KiteException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
            throw new Exception(e);
        }
        return quoteResponseTO;
    }
}
