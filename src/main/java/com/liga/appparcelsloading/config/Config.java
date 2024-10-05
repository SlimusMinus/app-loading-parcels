package com.liga.appparcelsloading.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Класс конфигурации Spring, содержащий определения бинов внешних библиотек.
 * Все бины создаются и управляются Spring контекстом.
 */
@Configuration
public class Config {
    /**
     * Создает бин {@link ObjectMapper}, используемый для сериализации и десериализации JSON-данных.
     *
     * @return экземпляр {@link ObjectMapper}
     */
    @Bean
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
