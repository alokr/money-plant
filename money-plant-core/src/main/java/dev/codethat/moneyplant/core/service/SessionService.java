package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.SessionRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

public interface SessionService<RequestT extends SessionRequestCoreTO, ResponseT extends SessionResponseCoreTO> {
    ResponseT login(RequestT requestTO) throws Exception;

    ResponseT refresh(RequestT requestTO);

    ResponseT logout(RequestT requestTO);
}
