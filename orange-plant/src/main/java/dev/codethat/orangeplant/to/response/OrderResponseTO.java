package dev.codethat.orangeplant.to.response;

import com.zerodhatech.models.Order;
import dev.codethat.moneyplant.core.to.response.OrderResponseCoreTO;
import lombok.Data;

@Data
public class OrderResponseTO extends OrderResponseCoreTO {
    private Order order;
}
