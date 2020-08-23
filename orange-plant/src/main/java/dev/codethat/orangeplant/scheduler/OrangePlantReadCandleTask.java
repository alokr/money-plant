package dev.codethat.orangeplant.scheduler;

import com.zerodhatech.models.OHLC;
import dev.codethat.moneyplant.core.scheduler.ReadCandleTask;
import dev.codethat.moneyplant.core.service.QuoteService;
import dev.codethat.moneyplant.core.spring.MoneyPlantApplicationProperties;
import dev.codethat.orangeplant.bean.request.QuoteRequestTO;
import dev.codethat.orangeplant.bean.response.QuoteResponseTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.ATRIndicator;

import javax.inject.Named;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Named
@Slf4j
@RequiredArgsConstructor
public class OrangePlantReadCandleTask implements ReadCandleTask {
    private final QuoteService<QuoteRequestTO, QuoteResponseTO> quoteService;

    private final MoneyPlantApplicationProperties moneyPlantApplicationProperties;

    private QuoteRequestTO quoteRequestTO;

    private BarSeries barSeries;

    private ATRIndicator atrIndicator;

    @Override
    public void run() {
        try {
            QuoteResponseTO quoteResponseTO = quoteService.ohlcQuote(quoteRequestTO);
            OHLC candleTick = quoteResponseTO.getOhlcQuoteMapMap().get(quoteRequestTO.getInstruments() [0]).ohlc;
            if (Objects.isNull(barSeries)) {
                barSeries = new BaseBarSeries();
                barSeries.setMaximumBarCount(moneyPlantApplicationProperties.getTechnical()
                        .getBarSeries().getBarCount());
                atrIndicator = new ATRIndicator(barSeries, moneyPlantApplicationProperties.getTechnical()
                        .getAtr().getBarCount());
            }
            barSeries.addBar(
                    ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                    , candleTick.open
                    , candleTick.high
                    , candleTick.low
                    , candleTick.close);
            System.out.println(atrIndicator.getValue(barSeries.getEndIndex()));
        } catch (Exception e) {
            log.error("Exception occurred. Message={}", e.getMessage());
        }
    }

    public void setQuoteRequestTO(QuoteRequestTO quoteRequestTO) {
        this.quoteRequestTO = quoteRequestTO;
    }
}
