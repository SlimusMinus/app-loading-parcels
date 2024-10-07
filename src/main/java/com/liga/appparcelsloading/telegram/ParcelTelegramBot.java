package com.liga.appparcelsloading.telegram;

import com.liga.appparcelsloading.config.BotConfig;
import com.liga.appparcelsloading.service.ParcelCommandTelegramService;
import com.liga.appparcelsloading.service.TruckCommandTelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class ParcelTelegramBot extends TelegramLongPollingBot {
    @Autowired
    private ParcelCommandTelegramService parcelCommandTelegramService;
    @Autowired
    private TruckCommandTelegramService truckCommandTelegramService;
    @Autowired
    private BotConfig config;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            processCommand(update, command, update.getMessage().getChatId());
        }
    }

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
            } else if (command.startsWith("/getParcels")) {
                parcelCommandTelegramService.handleGetParcelsCommand(message);
            } else if (command.startsWith("/getParcel")) {
                parcelCommandTelegramService.handleGetParcelCommand(command, message);
            } else if (command.startsWith("/deleteParcel")) {
                parcelCommandTelegramService.handleDeleteParcelCommand(command, message);
            } else if (command.startsWith("/getTrucks")) {
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
