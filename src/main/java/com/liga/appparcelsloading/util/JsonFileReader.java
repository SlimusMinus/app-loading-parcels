package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.liga.appparcelsloading.model.Truck;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonFileReader {
    /**
     * Универсальный метод для чтения данных из JSON файла
     *
     * @param fileName путь к файлу, который необходимо прочитать
     * @param typeRef  TypeReference для десериализации данных
     * @param <T>      тип возвращаемых данных
     * @return данные типа T, прочитанные из файла
     */
    public <T> List<T> read(String fileName, TypeReference<List<T>> typeRef) {
        List<T> dataList = new ArrayList<>();
        log.info("Начало чтения файла {}", fileName);
        try {
            dataList = ObjectMapperFactory.getInstance().readValue(new File(fileName), typeRef);
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
    public List<Truck> readTrucks(String fileName) {
        return read(fileName, new TypeReference<>() {});
    }

    /**
     * Читает данные char[][] из JSON файла
     *
     * @param fileName путь к файлу, который необходимо прочитать
     * @return список массивов char[][]
     */
    public List<char[][]> readParcels(String fileName) {
        return read(fileName, new TypeReference<>() {});
    }
}
