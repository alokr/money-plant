package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.to.request.AccountRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.AccountResponseCoreTO;

public interface AccountAdapter<RequestT extends AccountRequestCoreTO, ResponseT extends AccountResponseCoreTO> {
    ResponseT margin(RequestT requestTO) throws Exception;
}
