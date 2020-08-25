package dev.codethat.orangeplant.listener;

import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.OnTicks;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.listener.StreamingTickCoreListener;
import dev.codethat.moneyplant.core.task.BarGeneratorTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.ArrayList;

@Named
@AllArgsConstructor
@Slf4j
public class StreamingTickListener implements StreamingTickCoreListener, OnTicks {
    private BarGeneratorTask barGeneratorTask;

    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        ticks.stream().forEach(
                tick -> {
                    barGeneratorTask.getMoneyPlantTickList()
                            .add(new MoneyPlantTick(tick.getLastTradedPrice(), tick.getVolumeTradedToday()));
                }
        );
    }
}
