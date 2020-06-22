package dev.codethat.moneyplant.core.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.util.Scanner;

@Slf4j
public abstract class MoneyPlantCoreApplicationRunner implements CommandLineRunner {
    protected void shutdown(Scanner scanner) {
        log.info("Shutting down!");
        scanner.close();
        System.exit(0);
    }
}
