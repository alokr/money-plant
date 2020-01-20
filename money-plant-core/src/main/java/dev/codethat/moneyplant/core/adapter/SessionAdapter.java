package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.to.request.SessionRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

public interface SessionAdapter<RequestTO extends SessionRequestCoreTO, ResponseTO extends SessionResponseCoreTO> {
    ResponseTO login(RequestTO requestTO);

    ResponseTO refresh(RequestTO requestTO);

    ResponseTO logout(RequestTO requestTO);
}
