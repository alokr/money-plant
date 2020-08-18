package dev.codethat.orangeplant.bean.response;

import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;
import dev.codethat.moneyplant.core.bean.response.AccountResponseCoreTO;
import lombok.Data;

@Data
public class AccountResponseTO extends AccountResponseCoreTO {
    private Profile profile;

    private Margin margin;
}
