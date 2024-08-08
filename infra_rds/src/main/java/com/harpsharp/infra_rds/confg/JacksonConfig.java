package com.harpsharp.infra_rds.confg;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

    private final ObjectMapper objectMapper;
    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.getFactory().setCharacterEscapes(new JsonFactory().getCharacterEscapes());
        objectMapper.getFactory().setCodec(objectMapper);
    }
}