package dev.codethat.orangeplant.to.response;

import com.zerodhatech.models.Instrument;
import dev.codethat.moneyplant.core.to.response.QuoteResponseCoreTO;
import lombok.Data;

import java.util.List;

@Data
public class QuoteResponseTO extends QuoteResponseCoreTO {
    private List<Instrument> instruments;
}
