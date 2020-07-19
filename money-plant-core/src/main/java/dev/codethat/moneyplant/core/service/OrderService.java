package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.SessionRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

public interface OrderService<RequestT extends SessionRequestCoreTO, ResponseT extends SessionResponseCoreTO> {
    ResponseT place(RequestT requestTO) throws Exception;
}
