package com.harpsharp.infra_rds.confg;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.harpsharp.infra_rds.repository")
public class RDSConfig {
}
