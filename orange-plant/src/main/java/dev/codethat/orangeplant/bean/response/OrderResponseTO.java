package dev.codethat.orangeplant.bean.response;

import com.zerodhatech.models.Order;
import dev.codethat.moneyplant.core.bean.response.OrderResponseCoreTO;
import lombok.Data;

@Data
public class OrderResponseTO extends OrderResponseCoreTO {
    private Order order;
}
