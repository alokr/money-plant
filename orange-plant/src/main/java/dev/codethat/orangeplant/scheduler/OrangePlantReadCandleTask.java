package dev.codethat.orangeplant.scheduler;

import com.zerodhatech.models.OHLC;
import dev.codethat.moneyplant.core.bean.data.MarketData;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.service.QuoteService;
import dev.codethat.moneyplant.core.task.ReadCandleTask;
import dev.codethat.orangeplant.bean.request.QuoteRequestTO;
import dev.codethat.orangeplant.bean.response.QuoteResponseTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Slf4j
public class OrangePlantReadCandleTask implements ReadCandleTask {
    private final QuoteService quoteService;

    private QuoteRequestTO quoteRequestTO;

    private final MarketData marketData;

    @Inject
    public OrangePlantReadCandleTask(QuoteService quoteService, MarketData marketData) {
        this.quoteService = quoteService;
        this.marketData = marketData;
    }

    @Override
    public void run() {
        try {
            QuoteResponseTO quoteResponseTO = (QuoteResponseTO) quoteService.ohlcQuote(quoteRequestTO);
            OHLC ohlc = quoteResponseTO.getOhlcQuoteMapMap().get(quoteRequestTO.getInstruments() [0]).ohlc;
            marketData.addTick(new MoneyPlantTick(ohlc.close, 0));
        } catch (Exception e) {
            log.error("Exception occurred. Message={}", e.getMessage());
        }
    }

    public void setQuoteRequestTO(QuoteRequestTO quoteRequestTO) {
        this.quoteRequestTO = quoteRequestTO;
    }
}
