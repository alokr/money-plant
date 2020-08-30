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

    @Bean(MoneyPlantConstants.BEAN_CANDLE_READING_TASK_SCHEDULER)
    public TaskScheduler candleReadingTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix(MoneyPlantConstants.BEAN_CANDLE_READING_TASK_SCHEDULER);
        return scheduler;
    }

    @Bean(MoneyPlantConstants.BEAN_BAR_GENERATION_TASK_SCHEDULER)
    public TaskScheduler barGenerationTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix(MoneyPlantConstants.BEAN_BAR_GENERATION_TASK_SCHEDULER);
        return scheduler;
    }

    @Bean(MoneyPlantConstants.BEAN_MARKET_SIMULATION_TASK_SCHEDULER)
    public TaskScheduler marketSimulationTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix(MoneyPlantConstants.BEAN_MARKET_SIMULATION_TASK_SCHEDULER);
        return scheduler;
    }
}
