package dev.codethat.orangeplant.listener;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnError;
import dev.codethat.moneyplant.core.listener.StreamingConnectionStatusCoreListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StreamingConnectionStatusListener implements StreamingConnectionStatusCoreListener, OnConnect, OnDisconnect, OnError {
    @Override
    public void onConnected() {
        log.info("Streaming service connected");
    }

    @Override
    public void onDisconnected() {
        log.info("Streaming service disconnected");
    }

    @Override
    public void onError(Exception exception) {
        log.error("Exception occurred. Exception={}", exception.getMessage());
    }

    @Override
    public void onError(KiteException kiteException) {
        log.error("KiteException occurred. KiteException={}", kiteException.getMessage());
    }

    @Override
    public void onError(String error) {
        log.error("Error occurred. Error={}", error);
    }
}
