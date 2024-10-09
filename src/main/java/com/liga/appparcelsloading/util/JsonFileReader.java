package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.model.Truck;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс {@code JsonFileReader} отвечает за чтение данных из JSON файлов и десериализацию их в объекты.
 * Он использует {@link ObjectMapper} для преобразования данных JSON в Java объекты.
 * <p>
 * Класс предоставляет универсальный метод {@code read} для чтения любого типа данных, а также специализированный метод {@code readTrucks},
 * предназначенный для чтения списка объектов {@link Truck} из файла.
 * </p>
 *
 * <p><b>Пример использования:</b></p>
 * <pre>
 *     JsonFileReader jsonFileReader = new JsonFileReader(new ObjectMapper());
 *     List<Truck> trucks = jsonFileReader.readTrucks("trucks.json");
 * </pre>
 *
 * <p>Методы класса логируют начало и завершение процесса чтения файла, а также любые возникшие ошибки.</p>
 *
 */
@Component
@Slf4j
@AllArgsConstructor
public class JsonFileReader {
    private final ObjectMapper mapper;

    /**
     * Читает список грузовиков из JSON файла
     *
     * @param fileName путь к файлу, который необходимо прочитать
     * @return список объектов Truck
     */
    public List<Truck> readTrucks(String fileName) {
        return read(fileName, new TypeReference<>() {});
    }

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
}
