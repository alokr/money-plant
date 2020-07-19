package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.to.request.QuoteRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.QuoteResponseCoreTO;

public interface QuoteAdapter<RequestT extends QuoteRequestCoreTO, ResponseT extends QuoteResponseCoreTO> {
    ResponseT instruments(RequestT requestTO) throws Exception;

    ResponseT quote(RequestT requestTO) throws Exception;

    ResponseT ohlc(RequestT requestTO) throws Exception;

    ResponseT ltp(RequestT requestTO) throws Exception;
}
