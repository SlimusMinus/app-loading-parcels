package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.model.FullTruck;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class JsonFileReader {
    private final ObjectMapper mapper;

    /**
     * Универсальный метод для чтения данных из JSON файла
     *
     * @param fileName путь к файлу, который необходимо прочитать
     * @param typeRef  TypeReference для десериализации данных
     * @param <T>      тип возвращаемых данных
     * @return данные типа T, прочитанные из файла
     */
    private <T> List<T> read(String fileName, TypeReference<List<T>> typeRef) {
        List<T> dataList = new ArrayList<>();
        log.info("Начало чтения файла {}", fileName);
        try {
            dataList = mapper.readValue(new File(fileName), typeRef);
            log.info("Файл успешно прочитан: {}", fileName);
        } catch (IOException e) {
            log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
        }
        return dataList;
    }

    /**
     * Читает список грузовиков из JSON файла
     *
     * @param fileName путь к файлу, который необходимо прочитать
     * @return список объектов Truck
     */
    public List<FullTruck> readTrucks(String fileName) {
        return read(fileName, new TypeReference<>() {});
    }
}
