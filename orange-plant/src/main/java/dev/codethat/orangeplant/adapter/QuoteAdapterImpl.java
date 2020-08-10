package dev.codethat.orangeplant.adapter;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import dev.codethat.moneyplant.core.adapter.QuoteAdapter;
import dev.codethat.moneyplant.core.cache.MoneyPlantCache;
import dev.codethat.orangeplant.spring.OrangePlantApplicationProperties;
import dev.codethat.orangeplant.to.request.QuoteRequestTO;
import dev.codethat.orangeplant.to.response.QuoteResponseTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Named
@Slf4j
@AllArgsConstructor
public class QuoteAdapterImpl implements QuoteAdapter<QuoteRequestTO, QuoteResponseTO> {
    private final OrangePlantApplicationProperties applicationProperties;

    private final MoneyPlantCache moneyPlantCache;

    @Override
    public QuoteResponseTO instruments(QuoteRequestTO quoteRequestTO) throws Exception {
        KiteConnect kiteConnect = moneyPlantCache.brokerHttpClient();
        QuoteResponseTO quoteResponseTO = null;
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
        QuoteResponseTO quoteResponseTO = null;
        try {
            quoteResponseTO = new QuoteResponseTO();
            quoteRequestTO.setQuoteMap(kiteConnect.getQuote(quoteRequestTO.getInstruments()));
        } catch (KiteException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
            throw new Exception(e);
        }
        return quoteResponseTO;
    }

    @Override
    public QuoteResponseTO ohlc(QuoteRequestTO quoteRequestTO) throws Exception {
        return null;
    }

    @Override
    public QuoteResponseTO ltp(QuoteRequestTO quoteRequestTO) throws Exception {
        return null;
    }
}
