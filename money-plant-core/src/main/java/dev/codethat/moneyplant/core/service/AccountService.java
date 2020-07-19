package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.AccountRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.AccountResponseCoreTO;

public interface AccountService<RequestT extends AccountRequestCoreTO, ResponseT extends AccountResponseCoreTO> {
    ResponseT margin(RequestT requestTO) throws Exception;
}
