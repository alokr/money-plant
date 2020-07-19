package dev.codethat.moneyplant.core.adapter;

import dev.codethat.moneyplant.core.to.request.OrderRequestCoreTO;
import dev.codethat.moneyplant.core.to.response.OrderResponseCoreTO;

public interface OrderAdapter<RequestT extends OrderRequestCoreTO, ResponseT extends OrderResponseCoreTO> {
    ResponseT place(RequestT requestTO);

    ResponseT replace(RequestT requestTO);

    ResponseT order(RequestT requestTO);

    ResponseT cancel(RequestT requestTO);

    ResponseT orders(RequestT requestTO);
}
