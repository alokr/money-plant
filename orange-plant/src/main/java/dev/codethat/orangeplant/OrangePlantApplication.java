package dev.codethat.orangeplant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.codethat")
public class OrangePlantApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrangePlantApplication.class, args);
    }
}
