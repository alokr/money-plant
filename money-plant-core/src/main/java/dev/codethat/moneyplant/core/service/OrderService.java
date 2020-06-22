package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.SessionRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

public interface OrderService<RequestTO extends SessionRequestCoreTO, ResponseTO extends SessionResponseCoreTO> {
    ResponseTO place(RequestTO requestTO) throws Exception;
}
