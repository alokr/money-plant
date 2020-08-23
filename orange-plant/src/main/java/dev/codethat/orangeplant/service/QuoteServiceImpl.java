package dev.codethat.orangeplant.service;

import dev.codethat.moneyplant.core.adapter.QuoteAdapter;
import dev.codethat.moneyplant.core.service.QuoteService;
import dev.codethat.orangeplant.bean.request.QuoteRequestTO;
import dev.codethat.orangeplant.bean.response.QuoteResponseTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class QuoteServiceImpl implements QuoteService<QuoteRequestTO, QuoteResponseTO> {
    private final QuoteAdapter adapter;

    @Override
    public QuoteResponseTO instruments(QuoteRequestTO quoteRequestTO) throws Exception {
        return (QuoteResponseTO) adapter.instruments(quoteRequestTO);
    }

    @Override
    public QuoteResponseTO quote(QuoteRequestTO quoteRequestTO) throws Exception {
        return (QuoteResponseTO) adapter.quote(quoteRequestTO);
    }

    @Override
    public QuoteResponseTO ohlcQuote(QuoteRequestTO quoteRequestTO) throws Exception {
        return (QuoteResponseTO) adapter.ohlcQuote(quoteRequestTO);
    }

    @Override
    public QuoteResponseTO ltp(QuoteRequestTO quoteRequestTO) throws Exception {
        return null;
    }
}
