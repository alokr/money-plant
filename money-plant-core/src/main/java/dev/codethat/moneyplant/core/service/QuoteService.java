package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.AccountRequestCoreTO;
import dev.codethat.moneyplant.core.to.request.QuoteRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.AccountResponseCoreTO;
import dev.codethat.moneyplant.core.to.response.QuoteResponseCoreTO;

public interface QuoteService<RequestTO extends QuoteRequestCoreTO, ResponseTO extends QuoteResponseCoreTO> {
    ResponseTO instruments(RequestTO requestTO) throws Exception;

    ResponseTO instruments(RequestTO requestTO, String exchange) throws Exception;

    ResponseTO quote(RequestTO requestTO) throws Exception;

    ResponseTO ohlc(RequestTO requestTO) throws Exception;

    ResponseTO ltp(RequestTO requestTO) throws Exception;
}