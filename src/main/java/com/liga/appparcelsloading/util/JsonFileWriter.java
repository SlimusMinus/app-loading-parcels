package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.model.Truck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для записи информации о грузовиках в JSON файл.
 */
@Slf4j
@Component
public class JsonFileWriter {
    private final ObjectMapper mapper;

    public JsonFileWriter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Записывает список грузовиков в JSON файл, добавляя к уже существующим.
     *
     * @param fullTrucks список грузовиков для записи
     * @param fileName   имя файла для записи
     * @return обновленный список грузовиков
     */
    public List<Truck> write(List<Truck> fullTrucks, String fileName) {
        List<Truck> trucks = new ArrayList<>();
        try {
            File file = new File(fileName);
            if (file.exists()) {
                trucks = mapper.readValue(file, new TypeReference<>() {});
                log.info("Файл {} успешно прочитан, найдено {} грузовиков.", fileName, trucks.size());
            }
            trucks.addAll(fullTrucks);
            log.info("Добавлено {} новых грузовиков.", fullTrucks.size());

            mapper.writeValue(file, trucks);
            log.info("Дозапись в файл завершена: {}", fileName);
            System.out.println("Дозапись в файл завершена.");
        } catch (IOException e) {
            log.error("Ошибка при записи в файл {}: {}", fileName, e.getMessage());
        }
        return trucks;
    }
}
