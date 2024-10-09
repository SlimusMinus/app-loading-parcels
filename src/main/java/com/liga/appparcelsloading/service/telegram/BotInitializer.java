package com.liga.appparcelsloading.service.telegram;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Инициализирует Telegram-бота после завершения загрузки контекста Spring.
 * При возникновении события ContextRefreshedEvent регистрирует бота с использованием
 * TelegramBotsApi.
 */
@Slf4j
@Component
@AllArgsConstructor
public class BotInitializer {
    private ParcelTelegramBot telegramBot;

    /**
     * Метод, который вызывается при событии обновления контекста.
     * Регистрация Telegram-бота с использованием TelegramBotsApi.
     *
     * @throws TelegramApiException если произошла ошибка при регистрации бота.
     */
    @EventListener({ContextRefreshedEvent.class})
    public void initialize() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        log.info("Инициализация Telegram-бота...");
        try{
            telegramBotsApi.registerBot(telegramBot);
            log.info("Telegram-бот успешно зарегистрирован.");
        } catch (TelegramApiException e) {
            log.error("Ошибка при регистрации Telegram-бота: {}", e.getMessage(), e);
        }
    }
}
