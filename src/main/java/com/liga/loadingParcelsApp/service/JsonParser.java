package com.liga.loadingParcelsApp.service;

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
public class JsonParser {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Записывает список грузовиков в указанный JSON файл. Если файл уже существует,
     * данные из него будут обновлены новыми грузовиками.
     *
     * @param trucks   список грузовиков, которые нужно записать
     * @param fileName путь к файлу, в который будут записаны данные
     */
    public static void writeListTrucksInFile(List<Truck> trucks, String fileName) {
        List<Truck> existingTrucksList = new ArrayList<>();
        Path filePath = Path.of(fileName);
        // Чтение существующего файла, если он есть
        if (Files.exists(filePath)) {
            try {
                existingTrucksList = OBJECT_MAPPER.readValue(filePath.toFile(), new TypeReference<>() {
                });
            } catch (IOException e) {
                log.error("Ошибка чтения файла {}: {}", fileName, e);
            }
        }
        // Добавление новых грузовиков и запись в файл
        existingTrucksList.addAll(trucks);
        try {
            OBJECT_MAPPER.writeValue(filePath.toFile(), existingTrucksList);
            log.info("Список грузовиков успешно обновлен в файле {}", fileName);
        } catch (IOException e) {
            log.error("Ошибка записи в файл {}: {}", fileName, e);
        }
    }

    /**
     * Читает данные из JSON файла и выводит информацию о каждом грузовике
     * и его посылках в консоль.
     *
     * @param fileName путь к файлу, который необходимо прочитать
     */
    public static void readJsonTrucks(String fileName) {
        try {
            List<Truck> trucks = OBJECT_MAPPER.readValue(new File(fileName), new TypeReference<>() {
            });
            for (Truck truck : trucks) {
                System.out.println("Грузовик " + truck.getName() + " содержит");
                Map<Integer, Integer> parcels = new HashMap<>();
                final List<Integer> truckParcels = truck.getParcels();
                for (Integer parcel : truckParcels) {
                    parcels.merge(parcel, 1, Integer::sum);
                }
                parcels.forEach((size, count) ->
                        System.out.println(count + " посылки(у) размером " + size)
                );
            }
        } catch (IOException e) {
            log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
        }
    }
}
