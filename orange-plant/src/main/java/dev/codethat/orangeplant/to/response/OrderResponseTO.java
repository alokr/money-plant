package dev.codethat.orangeplant.to.response;

import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;
import dev.codethat.moneyplant.core.to.response.AccountResponseCoreTO;
import dev.codethat.moneyplant.core.to.response.OrderResponseCoreTO;
import lombok.Data;

@Data
public class OrderResponseTO extends OrderResponseCoreTO {
    private Profile profile;

    private Margin margin;
}
