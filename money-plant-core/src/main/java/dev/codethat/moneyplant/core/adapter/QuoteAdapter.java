package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.bean.request.QuoteRequestCoreTO;
import dev.codethat.moneyplant.core.bean.response.QuoteResponseCoreTO;

public interface QuoteAdapter<RequestT extends QuoteRequestCoreTO, ResponseT extends QuoteResponseCoreTO> {
    ResponseT instruments(RequestT requestTO) throws Exception;

    ResponseT quote(RequestT requestTO) throws Exception;

    ResponseT ohlcQuote(RequestT requestTO) throws Exception;

    ResponseT ltp(RequestT requestTO) throws Exception;
}
