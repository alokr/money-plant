package dev.codethat.orangeplant.bean.request;

import com.zerodhatech.models.Quote;
import dev.codethat.moneyplant.core.bean.request.QuoteRequestCoreTO;
import lombok.Data;

import java.util.Map;

@Data
public class QuoteRequestTO extends QuoteRequestCoreTO {
    private String[] instruments;
}
