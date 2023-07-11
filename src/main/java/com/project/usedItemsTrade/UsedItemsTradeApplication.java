package com.project.usedItemsTrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UsedItemsTradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsedItemsTradeApplication.class, args);
    }
}
