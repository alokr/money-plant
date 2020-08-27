package dev.codethat.orangeplant.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.List;

@Data
@ConfigurationProperties("orange-plant")
public class OrangePlantApplicationProperties {
    private String zoneOffset;
    private Market market;
    private KiteConnect kiteConnect;
    private TradePreference tradePreference;

    @Data
    public static class Market {
        private String eqExchange;
        private String futOptExchange;
    }

    @Data
    public static class KiteConnect {
        private String userId;
        private String loginUrl;
        private boolean loggingEnabled;
        private Api api;
        private QueryParams queryParams;
        private Account account;

        @Data
        public static class Account {
            private String segment;
        }

        @Data
        public static class Api {
            private String key;
            private String version;
            private String secret;
        }

        @Data
        public static class QueryParams {
            private String version;
            private String apiKey;
        }
    }

    @Data
    public static class TradePreference {
        private String exchange;
        private List<String> segments;
        private String instrument;
        private Date expiryStartDate;
        private Date expiryEndDate;
    }
}
