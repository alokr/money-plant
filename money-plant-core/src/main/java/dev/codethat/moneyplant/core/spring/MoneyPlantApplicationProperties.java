package dev.codethat.moneyplant.core.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("money-plant")
public class MoneyPlantApplicationProperties {
    private Technical technical;
    private MarketData marketData;

    @Data
    public static class MarketData {
        private boolean tickerStreamingEnabled;
        private boolean tickerReadingEnabled;
        private boolean barGenerationEnabled;
        private long candlePeriod;
        private long tickerPeriod;
    }

    @Data
    public static class Technical {
        private BarSeries barSeries;
        private ATR atr;

        @Data
        public static class BarSeries {
            private int barCount;
        }

        @Data
        public static class ATR {
            private int barCount;
        }
    }
}
