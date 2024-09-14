package com.liga.loadingParcelsApp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.loadingParcelsApp.model.Truck;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonWriterTrucks {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void writeListTrucksInFile(List<Truck> trucks, String fileName) {
        List<Truck> existingTrucksList = new ArrayList<>();

        File file = new File(fileName);
        if (file.exists()) {
            try {
                existingTrucksList = OBJECT_MAPPER.readValue(file, new TypeReference<List<Truck>>() {
                });
            } catch (IOException e) {
                log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
            }
        }

        existingTrucksList.addAll(trucks);

        try {
            OBJECT_MAPPER.writeValue(file, existingTrucksList);
            log.info("Список грузовиков успешно обновлен в файле {}", fileName);
        } catch (IOException e) {
            log.error("Ошибка записи в файл {}: {}", fileName, e.getMessage());
        }
    }
}
