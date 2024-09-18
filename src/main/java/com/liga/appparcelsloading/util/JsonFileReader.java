package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.model.Truck;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonFileReader {
    /**
     * Читает данные из JSON файла
     *
     * @param fileName путь к файлу, который необходимо прочитать
     */
    public List<Truck> read(String fileName) {
        List<Truck> trucks = new ArrayList<>();
        log.info("Начало чтения файла {}", fileName);
        try {
            trucks = ObjectMapperFactory.getInstance().readValue(new File(fileName), new TypeReference<>() {});
            log.info("Файл успешно прочитан: {}", fileName);
        } catch (IOException e) {
            log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
        }
        return trucks;
    }
}
