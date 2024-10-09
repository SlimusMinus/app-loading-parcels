package com.liga.appparcelsloading.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Класс BotConfig представляет конфигурацию для Telegram-бота.
 * Загружает параметры конфигурации из файла application.properties.
 * Содержит параметры для имени и токена бота.
 */
@Configuration
@Data
@PropertySource("classpath:application.yml")
public class BotConfig {

    @Value("${telegram.bot.username}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String botToken;
}
