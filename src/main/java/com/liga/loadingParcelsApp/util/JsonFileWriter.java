package com.liga.loadingParcelsApp.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.loadingParcelsApp.model.Truck;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для чтения и записи JSON файлов, содержащих список грузовиков.
 * Использует ObjectMapper для сериализации и десериализации объектов в JSON формат.
 */
@Slf4j
public class JsonFileWriter {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Записывает список грузовиков в указанный JSON файл. Если файл уже существует,
     * данные из него будут обновлены новыми грузовиками.
     *
     * @param trucks   список грузовиков, которые нужно записать
     * @param fileName путь к файлу, в который будут записаны данные
     */
    public List<Truck> write(List<Truck> trucks, String fileName) {
        List<Truck> existingTrucksList = new ArrayList<>();
        Path filePath = Path.of(fileName);
        log.info("Начало записи списка грузовиков в файл {}", fileName);
        // Чтение существующего файла, если он есть
        if (Files.exists(filePath)) {
            try {
                existingTrucksList = OBJECT_MAPPER.readValue(filePath.toFile(), new TypeReference<>() {});
                log.info("Чтение существующих данных из файла {}", fileName);
            } catch (IOException e) {
                log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
            }
        }
        // Добавление новых грузовиков и запись в файл
        existingTrucksList.addAll(trucks);
        try {
            OBJECT_MAPPER.writeValue(filePath.toFile(), existingTrucksList);
            log.info("Список грузовиков успешно обновлен в файле {}", fileName);
        } catch (IOException e) {
            log.error("Ошибка записи в файл {}: {}", fileName, e.getMessage());
        }
        return existingTrucksList;
    }
}
