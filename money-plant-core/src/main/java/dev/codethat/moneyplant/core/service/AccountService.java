package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.AccountRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.AccountResponseCoreTO;

public interface AccountService<RequestTO extends AccountRequestCoreTO, ResponseTO extends AccountResponseCoreTO> {
    ResponseTO margin(RequestTO requestTO) throws Exception;
}
