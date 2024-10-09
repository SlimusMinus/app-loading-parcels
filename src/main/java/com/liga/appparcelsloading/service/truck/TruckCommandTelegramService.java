package com.liga.appparcelsloading.service.truck;

import com.liga.appparcelsloading.service.dto.TruckDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для обработки команд, связанных с грузовиками, в Telegram-боте.
 * Предоставляет методы для загрузки посылок в грузовики, получения информации о грузовиках и их удалении.
 */
@Service
@AllArgsConstructor
@Slf4j
public class TruckCommandTelegramService {

    private TruckRestService truckRestService;

    /**
     * Обрабатывает команду для загрузки посылок в грузовики по имени.
     *
     * @param update  обновление из Telegram, содержащее информацию о команде
     * @param message сообщение, которое будет отправлено в Telegram
     */
    public void handleLoadParcelsByNameCommand(Update update, SendMessage message) {
        String commandText = update.getMessage().getText();
        String[] parts = commandText.split(" ");
        if (parts.length < 5) {
            message.setText("Используйте команду в формате: /loadParcelsByName <algorithmType> <nameParcels> <heights> <weights>");
            return;
        }
        System.out.println(parts[1] + parts[2] + parts[3] + parts[4]);
        String algorithmType = parts[1];
        String nameParcels = parts[2];
        String heights = parts[3];
        String weights = parts[4];

        try {
            Optional<List<TruckDto>> trucks = truckRestService.loadByName(algorithmType, nameParcels, heights, weights);
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

    /**
     * Обрабатывает команду для загрузки посылок в грузовики.
     *
     * @param update  обновление из Telegram, содержащее информацию о команде
     * @param message сообщение, которое будет отправлено в Telegram
     */
    public void handleLoadParcelInTruckCommand(Update update, SendMessage message) {
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
            Optional<List<TruckDto>> trucks = truckRestService.load(algorithmType, heights, weights);
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

    /**
     * Обрабатывает команду для удаления грузовика по ID.
     *
     * @param command команда для удаления грузовика
     * @param message сообщение, которое будет отправлено в Telegram
     */
    public void handleDeleteTruckByCommand(String command, SendMessage message) {
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

    /**
     * Обрабатывает команду для получения информации о грузовике по ID.
     *
     * @param command команда для получения информации о грузовике
     * @param message сообщение, которое будет отправлено в Telegram
     */
    public void handleGetTruckByIdCommand(String command, SendMessage message) {
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

    /**
     * Обрабатывает команду для получения списка всех грузовиков.
     *
     * @param message сообщение, которое будет отправлено в Telegram
     */
    public void handleGetTrucksCommand(SendMessage message) {
        List<TruckDto> parcels = truckRestService.findAll().getBody();
        assert parcels != null;
        message.setText(parcels.toString());
    }
}
