package dev.codethat.orangeplant.listener;

import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.OnTicks;
import dev.codethat.moneyplant.core.bean.data.MoneyPlantTick;
import dev.codethat.moneyplant.core.listener.StreamingTickCoreListener;
import dev.codethat.moneyplant.core.task.BarGeneratorTask;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;

@Named
@Slf4j
public class StreamingTickListener implements StreamingTickCoreListener, OnTicks {
    private BarGeneratorTask barGeneratorTask;

    @Inject
    public StreamingTickListener(BarGeneratorTask barGeneratorTask) {
        this.barGeneratorTask = barGeneratorTask;
    }

    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        ticks.stream().forEach(
                tick -> {
                    synchronized (barGeneratorTask.getMoneyPlantTickList()) {
                        // log.info("tickCount={} ltp={} time={}", ticks.size(), tick.getLastTradedPrice(), new Date());
                        barGeneratorTask.getMoneyPlantTickList()
                                .add(new MoneyPlantTick(tick.getLastTradedPrice(), tick.getVolumeTradedToday()));
                    }
                }
        );
    }
}
