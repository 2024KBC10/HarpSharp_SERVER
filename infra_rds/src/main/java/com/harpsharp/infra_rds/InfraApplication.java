package com.harpsharp.infra_rds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.harpsharp.infra_rds")
public class InfraApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfraApplication.class, args);
    }

}
