package dev.codethat.orangeplant.to.request;

import com.zerodhatech.models.OrderParams;
import dev.codethat.moneyplant.core.to.request.OrderRequestCoreTO;
import lombok.Data;

@Data
public class OrderRequestTO extends OrderRequestCoreTO {
    OrderParams orderParams;
}
