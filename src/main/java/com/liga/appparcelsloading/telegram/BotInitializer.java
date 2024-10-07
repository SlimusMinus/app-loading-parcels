package com.liga.appparcelsloading.telegram;

import lombok.AllArgsConstructor;
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
        try{
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
