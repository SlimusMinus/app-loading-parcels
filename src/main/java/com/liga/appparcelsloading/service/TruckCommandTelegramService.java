package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.dto.TruckDto;
import com.liga.appparcelsloading.model.Truck;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TruckCommandTelegramService {

    private TruckRestService truckRestService;


    public void handleLoadParcelsByNameCommand(Update update, SendMessage message) {
        String commandText = update.getMessage().getText();
        String[] parts = commandText.split(" ");
        if (parts.length < 5) {
            message.setText("Используйте команду в формате: /loadParcelsByName <algorithmType> <nameParcels> <heights> <weights>");
            return;
        }
        System.out.println(parts[1] + parts[2] + parts[3] + parts[4]);
        String algorithmType = parts[1];
        String nameParcels = parts[2];  // Здесь у нас только названия посылок
        String heights = parts[3];  // Здесь должны быть только высоты (числа)
        String weights = parts[4];  // Здесь должны быть только веса (числа)

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

    public void handleGetTrucksCommand(SendMessage message) {
        List<TruckDto> parcels = truckRestService.findAll().getBody();
        assert parcels != null;
        message.setText(parcels.toString());
    }
}
