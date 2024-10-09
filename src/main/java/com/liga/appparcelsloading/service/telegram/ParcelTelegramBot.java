package com.liga.appparcelsloading.service.telegram;

import com.liga.appparcelsloading.config.BotConfig;
import com.liga.appparcelsloading.service.truck.TruckCommandTelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Основной класс для Telegram-бота, который обрабатывает обновления и команды пользователей.
 * Унаследован от {@link TelegramLongPollingBot}.
 */
@Component
@Slf4j
public class ParcelTelegramBot extends TelegramLongPollingBot {
    private final ParcelCommandTelegramService parcelCommandTelegramService;
    private final TruckCommandTelegramService truckCommandTelegramService;
    private final BotConfig config;

    public ParcelTelegramBot(BotConfig config, ParcelCommandTelegramService parcelCommandTelegramService, TruckCommandTelegramService truckCommandTelegramService) {
        this.parcelCommandTelegramService = parcelCommandTelegramService;
        this.truckCommandTelegramService = truckCommandTelegramService;
        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "Приветствие"));
        commands.add(new BotCommand("/getparcels", "Получить все посылки из базы"));
        commands.add(new BotCommand("/gettrucks", "Получить все загруженные грузовики из базы"));
        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка ввода команд {}", e.getMessage());
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    /**
     * Обрабатывает полученные обновления от Telegram.
     * Если обновление содержит текстовое сообщение, передает его для дальнейшей обработки.
     *
     * @param update обновление, полученное от Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            processCommand(update, command, update.getMessage().getChatId());
        }
    }

    /**
     * Обрабатывает команды, отправленные пользователем.
     * Вызывает соответствующий сервис для обработки команды и отправляет ответное сообщение.
     *
     * @param update  обновление, содержащее информацию о команде.
     * @param command команда, отправленная пользователем.
     * @param chatId  идентификатор чата, в который будет отправлено сообщение.
     */
    private void processCommand(Update update, String command, Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        try {
            if (command.startsWith("/start")) {
                parcelCommandTelegramService.handleStartCommand(update, message);
            } else if (command.startsWith("/createParcel")) {
                parcelCommandTelegramService.handleCreateParcelCommand(update, message);
            } else if (command.startsWith("/updateParcel")) {
                parcelCommandTelegramService.handleUpdateParcelCommand(command, message);
            } else if (command.startsWith("/getparcels")) {
                parcelCommandTelegramService.handleGetParcelsCommand(message);
            } else if (command.startsWith("/getParcel")) {
                parcelCommandTelegramService.handleGetParcelCommand(command, message);
            } else if (command.startsWith("/deleteParcel")) {
                parcelCommandTelegramService.handleDeleteParcelCommand(command, message);
            } else if (command.startsWith("/gettrucks")) {
                truckCommandTelegramService.handleGetTrucksCommand(message);
            } else if (command.startsWith("/loadParcelsByName")) {
                truckCommandTelegramService.handleLoadParcelsByNameCommand(update, message);
            } else if (command.startsWith("/loadParcels")) {
                truckCommandTelegramService.handleLoadParcelInTruckCommand(update, message);
            } else if (command.startsWith("/getTruck")) {
                truckCommandTelegramService.handleGetTruckByIdCommand(command, message);
            } else if (command.startsWith("/deleteTruck")) {
                truckCommandTelegramService.handleDeleteTruckByCommand(command, message);
            } else {
                message.setText("Неизвестная команда. Попробуйте снова.");
            }
        } catch (Exception e) {
            message.setText("Произошла ошибка: " + e.getMessage());
            log.error("Ошибка при обработке команды: ", e);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения в Telegram: ", e);
        }
    }
}
