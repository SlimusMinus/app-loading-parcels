package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.dto.ParcelDto;
import com.liga.appparcelsloading.model.Parcel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@AllArgsConstructor
public class ParcelCommandTelegramService {

    private final ParcelRestService service;

    public void handleStartCommand(Update update, SendMessage message) {
        message.setText("Hello " + update.getMessage().getChat().getFirstName() + "!");
    }

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
        Parcel parcel = new Parcel(name, symbol, form);
        ResponseEntity<Parcel> response = service.create(parcel);
        if (response.getStatusCode() == HttpStatus.OK) {
            message.setText("Посылка создана успешно!");
        } else {
            message.setText("Ошибка при создании посылки.");
        }
    }

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

            Parcel parcel = new Parcel(name, symbol, form);
            ResponseEntity<Parcel> response = service.update(id, parcel);

            if (response.getStatusCode() == HttpStatus.OK) {
                message.setText("Посылка обновлена успешно!");
            } else {
                message.setText("Ошибка при обновлении посылки.");
            }
        } catch (NumberFormatException e) {
            message.setText("Пожалуйста, введите корректный ID.");
        }
    }

    public void handleGetParcelsCommand(SendMessage message) {
        List<ParcelDto> parcels = service.findAll().getBody();
        message.setText(parcels.toString());
    }

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
