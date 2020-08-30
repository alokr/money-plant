package dev.codethat.moneyplant.core.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("money-plant")
public class MoneyPlantApplicationProperties {
    private Technical technical;
    private MarketData marketData;
    private MarketSimulation marketSimulation;

    @Data
    public static class MarketData {
        private boolean tickerReadingEnabled;
        private boolean tickerStreamingEnabled;
        private long tickerPeriod;
        private boolean barGenerationEnabled;
        private long candlePeriod;
    }

    @Data
    public static class MarketSimulation {
        private boolean isEnabled;
        private long simulationPeriod;
        private double lastTradedPrice;
        private long volumeTraded;
        double ltpStartRange;
        double ltpEndRange;
    }

    @Data
    public static class Technical {
        private BarSeries barSeries;
        private ATR atr;
        private SuperTrend superTrend;

        @Data
        public static class BarSeries {
            private int barCount;
        }

        @Data
        public static class ATR {
            private int barCount;
        }

        @Data
        public static class SuperTrend {
            private double multiplier;
        }
    }
}
