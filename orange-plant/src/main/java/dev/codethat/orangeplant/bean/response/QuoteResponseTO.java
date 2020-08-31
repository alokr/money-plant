package dev.codethat.orangeplant.bean.response;

import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.LTPQuote;
import com.zerodhatech.models.OHLCQuote;
import com.zerodhatech.models.Quote;
import dev.codethat.moneyplant.core.bean.response.QuoteResponseCoreTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QuoteResponseTO extends QuoteResponseCoreTO {
    private List<Instrument> instruments;

    private Map<String, Quote> quoteMap;

    private Map<String, OHLCQuote> ohlcQuoteMapMap;

    private Map<String, LTPQuote> ltpQuoteMap;
}
