package dev.codethat.orangeplant.to.request;

import com.zerodhatech.models.Quote;
import dev.codethat.moneyplant.core.to.request.AccountRequestCoreTO;
import dev.codethat.moneyplant.core.to.request.QuoteRequestCoreTO;
import lombok.Data;

import java.util.Map;

@Data
public class QuoteRequestTO extends QuoteRequestCoreTO {
    private String[] instruments;

    private Map<String, Quote> quoteMap;
}
