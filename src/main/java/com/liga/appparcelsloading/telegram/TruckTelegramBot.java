package com.liga.appparcelsloading.telegram;

import com.liga.appparcelsloading.config.BotConfig;
import com.liga.appparcelsloading.dto.TruckDto;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.TruckRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TruckTelegramBot  {
   /* @Autowired
    private TruckRestService truckRestService;
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
           if (command.startsWith("/getTrucks")) {
                handleGetTrucksCommand(message);
            } else if (command.startsWith("/loadParcels")) {
                handleLoadParcelInTruckCommand(update, message);
            } else if (command.startsWith("/loadParcelsByName")) {
                handleLoadParcelsByNameCommand(update, message);
            } else if (command.startsWith("/getTruck")) {
                handleGetTruckByIdCommand(command, message);
            } else if (command.startsWith("/deleteTruck")) {
                handleDeleteTruckByCommand(command, message);
            } else {
                message.setText("Неизвестная команда. Попробуйте снова.");
            }
        } catch (Exception e) {
            message.setText("Произошла ошибка: " + e.getMessage());
            log.error("Ошибка при обработке команды: ", e);
        }
    }

    private void handleLoadParcelsByNameCommand(Update update, SendMessage message) {
        String commandText = update.getMessage().getText();
        String[] parts = commandText.split(" ");
        if (parts.length < 5) {
            message.setText("Используйте команду в формате: /loadParcelsByName <algorithmType> <nameParcels> <heights> <weights>");
            return;
        }
        String algorithmType = parts[1];
        String nameParcels = parts[2];
        String heights = parts[3];
        String weights = parts[4];

        try {
            Optional<List<Truck>> trucks = truckRestService.loadByName(algorithmType, nameParcels, heights, weights);
            if (trucks.isPresent()) {
                message.setText("Посылки успешно загружены в грузовики: " + trucks.get());
            } else {
                message.setText("Ошибка: не удалось загрузить посылки с использованием данного алгоритма.");
            }
        } catch (Exception e) {
            message.setText("Произошла ошибка при загрузке посылок: " + e.getMessage());
            log.error("Ошибка при загрузке посылок по имени: ", e);
        }
    }

    private void handleLoadParcelInTruckCommand(Update update, SendMessage message) {
        String commandText = update.getMessage().getText();
        String[] parts = commandText.split(" ");

        if (parts.length < 4) {
            message.setText("Используйте команду в формате: /loadParcels <algorithmType> <heights> <weights>");
            return;
        }

        String algorithmType = parts[1];
        String heights = parts[2];
        String weights = parts[3];

        try {
            Optional<List<Truck>> trucks = truckRestService.load(algorithmType, heights, weights);
            if (trucks.isPresent()) {
                message.setText("Посылки успешно загружены в грузовики: " + trucks.get());
            } else {
                message.setText("Ошибка: не удалось загрузить посылки с использованием данного алгоритма.");
            }
        } catch (Exception e) {
            message.setText("Произошла ошибка при загрузке посылок: " + e.getMessage());
            log.error("Ошибка при загрузке посылок: ", e);
        }
    }

    private void handleDeleteTruckByCommand(String command, SendMessage message) {
        String[] parts = command.split(" ");
        if (parts.length == 2) {
            try {
                int id = Integer.parseInt(parts[1]);
                ResponseEntity<Void> response = truckRestService.deleteById(id);
                if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                    message.setText("Машина с ID " + id + " успешно удалена.");
                } else {
                    message.setText("Не удалось удалить машину с ID " + id + ". Она может не существовать.");
                }
            } catch (NumberFormatException e) {
                message.setText("Пожалуйста, введите корректный ID.");
            }
        } else {
            message.setText("Используйте команду в формате: /deleteTruck <id>");
        }
    }

    private void handleGetTruckByIdCommand(String command, SendMessage message) {
        String[] parts = command.split(" ");
        if (parts.length == 2) {
            try {
                int id = Integer.parseInt(parts[1]);
                TruckDto truckDto = truckRestService.findById(id).getBody();
                if (truckDto != null) {
                    message.setText("Машина найдена: " + truckDto);
                } else {
                    message.setText("Машина с ID " + id + " не найдена.");
                }
            } catch (NumberFormatException e) {
                message.setText("Пожалуйста, введите корректный ID.");
            }
        } else {
            message.setText("Используйте команду в формате: /getTruck <id>");
        }
    }

    private void handleGetTrucksCommand(SendMessage message) {
        List<TruckDto> parcels = truckRestService.findAll().getBody();
        assert parcels != null;
        message.setText(parcels.toString());
    }*/
}
