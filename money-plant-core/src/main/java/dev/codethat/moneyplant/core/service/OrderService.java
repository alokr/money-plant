package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.to.request.OrderRequestCoreTO;
import dev.codethat.moneyplant.core.to.request.SessionRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.OrderResponseCoreTO;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

public interface OrderService<RequestT extends OrderRequestCoreTO, ResponseT extends OrderResponseCoreTO> {
    ResponseT place(RequestT requestTO) throws Exception;

    ResponseT replace(RequestT requestTO) throws Exception;

    ResponseT cancel(RequestT requestTO) throws Exception;

    ResponseT orders(RequestT requestTO);
}
