package dev.codethat.orangeplant.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OrangePlantApplicationProperties.class)
public class OrangePlantApplicationConfiguration {

}
