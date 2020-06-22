package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.SessionRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

public interface SessionService<RequestTO extends SessionRequestCoreTO, ResponseTO extends SessionResponseCoreTO> {
    ResponseTO login(RequestTO requestTO) throws Exception;

    ResponseTO refresh(RequestTO requestTO);

    ResponseTO logout(RequestTO requestTO);
}
