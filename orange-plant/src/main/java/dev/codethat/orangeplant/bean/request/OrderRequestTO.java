package dev.codethat.orangeplant.bean.request;

import com.zerodhatech.models.OrderParams;
import dev.codethat.moneyplant.core.bean.request.OrderRequestCoreTO;
import lombok.Data;

@Data
public class OrderRequestTO extends OrderRequestCoreTO {
    OrderParams orderParams;
}
