package dev.codethat.orangeplant.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("orangeplant")
public class OrangePlantApplicationProperties {
    private KiteConnect kiteConnect;

    public KiteConnect getKiteConnect() {
        return kiteConnect;
    }

    public void setKiteConnect(KiteConnect kiteConnect) {
        this.kiteConnect = kiteConnect;
    }

    public static class KiteConnect {
        private String userId;
        private String loginUrl;
        private boolean loggingEnabled;
        private Api api;
        private QueryParams queryParams;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isLoggingEnabled() {
            return loggingEnabled;
        }

        public void setLoggingEnabled(boolean loggingEnabled) {
            this.loggingEnabled = loggingEnabled;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }

        public Api getApi() {
            return api;
        }

        public void setApi(Api api) {
            this.api = api;
        }

        public QueryParams getQueryParams() {
            return queryParams;
        }

        public void setQueryParams(QueryParams queryParams) {
            this.queryParams = queryParams;
        }

        public static class Api {
            private String key;
            private String version;
            private String secret;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }
        }

        public static class QueryParams {
            private String version;
            private String apiKey;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }
        }

    }

}
