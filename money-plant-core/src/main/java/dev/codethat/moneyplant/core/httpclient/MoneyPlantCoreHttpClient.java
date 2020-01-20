package dev.codethat.moneyplant.core.httpclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named
public class MoneyPlantCoreHttpClient {
    private final RestTemplate restTemplate;

    @Inject
    public MoneyPlantCoreHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<String> get(String url, Class<String> responseType, Map<String, String> uriMap) {
        return restTemplate.getForEntity(url, responseType, uriMap);
    }
}
