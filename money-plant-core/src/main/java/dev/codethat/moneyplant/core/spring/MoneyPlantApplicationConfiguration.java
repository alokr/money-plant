package dev.codethat.moneyplant.core.spring;

import dev.codethat.moneyplant.core.constants.MoneyPlantConstants;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(MoneyPlantApplicationProperties.class)
public class MoneyPlantApplicationConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean(MoneyPlantConstants.BEAN_READ_CANDLE_TASK_SCHEDULER)
    public TaskScheduler readCandleTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix(MoneyPlantConstants.BEAN_READ_CANDLE_TASK_SCHEDULER);
        return scheduler;
    }

    @Bean(MoneyPlantConstants.BEAN_BAR_GENERATOR_TASK_SCHEDULER)
    public TaskScheduler barGeneratorTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix(MoneyPlantConstants.BEAN_BAR_GENERATOR_TASK_SCHEDULER);
        return scheduler;
    }
}
