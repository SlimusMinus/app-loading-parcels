package com.liga.appparcelsloading.service.telegram;

import com.liga.appparcelsloading.service.dto.ParcelDto;
import com.liga.appparcelsloading.service.mapper.ParcelMapper;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.parcel.ParcelRestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Сервис для обработки команд, связанных с посылками, в Telegram.
 *
 * Класс {@code ParcelCommandTelegramService} предоставляет методы для обработки
 * команд, таких как создание, обновление, получение и удаление посылок,
 * а также отправляет соответствующие сообщения пользователю.
 */
@Service
@AllArgsConstructor
public class ParcelCommandTelegramService {

    private final ParcelRestService service;

    /**
     * Обрабатывает команду /start и отправляет приветственное сообщение.
     *
     * @param update  обновление, содержащее информацию о сообщении
     * @param message сообщение, которое будет отправлено пользователю
     */
    public void handleStartCommand(Update update, SendMessage message) {
        message.setText("Hello " + update.getMessage().getChat().getFirstName() + "!");
    }

    /**
     * Обрабатывает команду /createParcel для создания новой посылки.
     *
     * @param update  обновление, содержащее информацию о сообщении
     * @param message сообщение, которое будет отправлено пользователю
     */
    public void handleCreateParcelCommand(Update update, SendMessage message) {
        String commandText = update.getMessage().getText();
        String[] parts = commandText.split(" ", 4);
        if (parts.length < 4) {
            message.setText("Используйте команду в формате: /createParcel <name> <symbol> <form>");
            return;
        }
        String name = parts[1];
        char symbol = parts[2].charAt(0);
        String formData = parts[3];
        int[][] form = parseFormData(formData);
        if (form == null) {
            message.setText("Неверный формат данных формы. Пожалуйста, используйте формат: [[x1, y1], [x2, y2]]");
            return;
        }
        ParcelDto parcel = ParcelMapper.INSTANCE.toParcelDto(new Parcel(name, symbol, form));
        ResponseEntity<ParcelDto> response = service.create(parcel);
        if (response.getStatusCode() == HttpStatus.OK) {
            message.setText("Посылка создана успешно!");
        } else {
            message.setText("Ошибка при создании посылки.");
        }
    }

    /**
     * Обрабатывает команду /updateParcel для обновления существующей посылки.
     *
     * @param command команда, содержащая информацию для обновления посылки
     * @param message сообщение, которое будет отправлено пользователю
     */
    public void handleUpdateParcelCommand(String command, SendMessage message) {
        String[] parts = command.split(" ", 5);
        if (parts.length < 5) {
            message.setText("Используйте команду в формате: /updateParcel <id> <name> <symbol> <form>");
            return;
        }

        try {
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            char symbol = parts[3].charAt(0);
            String formData = parts[4];
            int[][] form = parseFormData(formData);

            if (form == null) {
                message.setText("Неверный формат данных формы. Пожалуйста, используйте формат: [[x1, y1], [x2, y2]]");
                return;
            }

            ParcelDto parcelDto = ParcelMapper.INSTANCE.toParcelDto(new Parcel(name, symbol, form));
            ResponseEntity<ParcelDto> response = service.update(id, parcelDto);

            if (response.getStatusCode() == HttpStatus.OK) {
                message.setText("Посылка обновлена успешно!");
            } else {
                message.setText("Ошибка при обновлении посылки.");
            }
        } catch (NumberFormatException e) {
            message.setText("Пожалуйста, введите корректный ID.");
        }
    }

    /**
     * Обрабатывает команду /getParcels для получения списка всех посылок.
     *
     * @param message сообщение, которое будет отправлено пользователю
     */
    public void handleGetParcelsCommand(SendMessage message) {
        List<ParcelDto> parcels = service.findAll().getBody();
        message.setText(parcels.toString());
    }

    /**
     * Обрабатывает команду /getParcel для получения информации о конкретной посылке по ID.
     *
     * @param command команда, содержащая ID посылки для получения
     * @param message сообщение, которое будет отправлено пользователю
     */
    public void handleGetParcelCommand(String command, SendMessage message) {
        String[] parts = command.split(" ");
        if (parts.length == 2) {
            try {
                int id = Integer.parseInt(parts[1]);
                ParcelDto parcel = service.findById(id).getBody();
                if (parcel != null) {
                    message.setText("Посылка найдена: " + parcel);
                } else {
                    message.setText("Посылка с ID " + id + " не найдена.");
                }
            } catch (NumberFormatException e) {
                message.setText("Пожалуйста, введите корректный ID.");
            }
        } else {
            message.setText("Используйте команду в формате: /getParcel <id>");
        }
    }

    /**
     * Обрабатывает команду /deleteParcel для удаления посылки по ID.
     *
     * @param command команда, содержащая ID посылки для удаления
     * @param message сообщение, которое будет отправлено пользователю
     */
    public void handleDeleteParcelCommand(String command, SendMessage message) {
        String[] parts = command.split(" ");
        if (parts.length == 2) {
            try {
                int id = Integer.parseInt(parts[1]);
                ResponseEntity<Void> response = service.deleteById(id);
                if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                    message.setText("Посылка с ID " + id + " успешно удалена.");
                } else {
                    message.setText("Не удалось удалить посылку с ID " + id + ". Она может не существовать.");
                }
            } catch (NumberFormatException e) {
                message.setText("Пожалуйста, введите корректный ID.");
            }
        } else {
            message.setText("Используйте команду в формате: /deleteParcel <id>");
        }
    }

    /**
     * Парсит строковые данные формы в двумерный массив целых чисел.
     *
     * @param formData строковые данные формы в формате [[x1, y1], [x2, y2]]
     * @return двумерный массив целых чисел или null, если формат неверный
     */
    private int[][] parseFormData(String formData) {
        try {
            formData = formData.trim().replaceAll("[\\[\\]]", "");
            String[] pairs = formData.split("\\],\\s*\\[");
            int[][] form = new int[pairs.length][];

            for (int i = 0; i < pairs.length; i++) {
                String[] values = pairs[i].split(",");
                form[i] = new int[values.length];
                for (int j = 0; j < values.length; j++) {
                    form[i][j] = Integer.parseInt(values[j].trim());
                }
            }
            return form;
        } catch (Exception e) {
            return null;
        }
    }
}
