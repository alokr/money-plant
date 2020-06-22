package dev.codethat.orangeplant.to.response;

import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;
import dev.codethat.moneyplant.core.to.response.AccountResponseCoreTO;
import lombok.Data;

@Data
public class AccountResponseTO extends AccountResponseCoreTO {
    private Profile profile;

    private Margin margin;
}
