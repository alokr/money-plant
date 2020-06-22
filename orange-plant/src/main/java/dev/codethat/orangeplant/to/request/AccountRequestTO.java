package dev.codethat.orangeplant.to.request;

import dev.codethat.moneyplant.core.to.request.AccountRequestCoreTO;
import lombok.Data;

@Data
public class AccountRequestTO extends AccountRequestCoreTO {
    private String segment;
}
