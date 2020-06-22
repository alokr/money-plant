package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.to.request.OrderRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.OrderResponseCoreTO;

public interface OrderAdapter<RequestTO extends OrderRequestCoreTO, ResponseTO extends OrderResponseCoreTO> {
    ResponseTO place(RequestTO requestTO);

    ResponseTO replace(RequestTO requestTO);

    ResponseTO order(RequestTO requestTO);

    ResponseTO cancel(RequestTO requestTO);

    ResponseTO orders(RequestTO requestTO);
}
