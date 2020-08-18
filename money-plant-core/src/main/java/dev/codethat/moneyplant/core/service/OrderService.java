package dev.codethat.moneyplant.core.service;

import dev.codethat.moneyplant.core.bean.request.OrderRequestCoreTO;
import dev.codethat.moneyplant.core.bean.response.OrderResponseCoreTO;

public interface OrderService<RequestT extends OrderRequestCoreTO, ResponseT extends OrderResponseCoreTO> {
    ResponseT place(RequestT requestTO) throws Exception;

    ResponseT replace(RequestT requestTO) throws Exception;

    ResponseT cancel(RequestT requestTO) throws Exception;

    ResponseT orders(RequestT requestTO);
}
