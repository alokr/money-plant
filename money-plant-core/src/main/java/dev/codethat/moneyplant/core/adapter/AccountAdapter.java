package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.to.request.AccountRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.AccountResponseCoreTO;

public interface AccountAdapter<RequestTO extends AccountRequestCoreTO, ResponseTO extends AccountResponseCoreTO> {
    ResponseTO margin(RequestTO requestTO) throws Exception;
}
