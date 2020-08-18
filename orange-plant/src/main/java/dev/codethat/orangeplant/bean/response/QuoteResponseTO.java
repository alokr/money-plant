package dev.codethat.orangeplant.bean.response;

import com.zerodhatech.models.Instrument;
import dev.codethat.moneyplant.core.bean.response.QuoteResponseCoreTO;
import lombok.Data;

import java.util.List;

@Data
public class QuoteResponseTO extends QuoteResponseCoreTO {
    private List<Instrument> instruments;
}
