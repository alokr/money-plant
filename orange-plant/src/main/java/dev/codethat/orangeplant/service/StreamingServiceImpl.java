package dev.codethat.orangeplant.service;

import dev.codethat.moneyplant.core.adapter.StreamingAdapter;
import dev.codethat.moneyplant.core.service.StreamingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StreamingServiceImpl implements StreamingService {
    private StreamingAdapter adapter;

    @Override
    public void init() {
        adapter.init();
    }

    @Override
    public void connect() {
        adapter.connect();
    }

    @Override
    public <T> void subscribe(List<T> tickerIds) {
        adapter.subscribe(tickerIds);
    }

    @Override
    public <T> void unsubscribe(List<T> tickerIds) {
        adapter.unsubscribe(tickerIds);
    }

    @Override
    public void disconnect() {
        adapter.disconnect();
    }
}
