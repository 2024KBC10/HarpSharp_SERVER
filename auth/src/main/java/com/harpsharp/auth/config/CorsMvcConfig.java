package com.harpsharp.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("http://swagger-auth:8080")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
