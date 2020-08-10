package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.to.request.OrderRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.OrderResponseCoreTO;

public interface OrderAdapter<RequestT extends OrderRequestCoreTO, ResponseT extends OrderResponseCoreTO> {
    ResponseT place(RequestT requestTO) throws Exception;

    ResponseT replace(RequestT requestTO);

    ResponseT cancel(RequestT requestTO);

    ResponseT orders(RequestT requestTO);

}
