package dev.codethat.orangeplant.bean.request;

import dev.codethat.moneyplant.core.bean.request.AccountRequestCoreTO;
import lombok.Data;

@Data
public class AccountRequestTO extends AccountRequestCoreTO {
    private String segment;
}
