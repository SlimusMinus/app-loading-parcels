package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.liga.appparcelsloading.model.Truck;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для чтения и записи JSON файлов, содержащих список грузовиков.
 * Использует ObjectMapper для сериализации и десериализации объектов в JSON формат.
 */
@Slf4j
@Service
@AllArgsConstructor
public class JsonFileWriter {
    private final ObjectMapper mapper;

    /**
     * Универсальный метод для записи данных в JSON файл.
     * Если файл уже существует, данные из него будут обновлены новыми записями.
     *
     * @param data     данные, которые нужно записать
     * @param fileName путь к файлу, в который будут записаны данные
     * @param typeRef  тип данных для десериализации (например, List<Truck> или List<char[][]>)
     * @param <T>      тип данных, которые записываются
     * @return обновленный список записей
     */
    public <T> List<T> write(List<T> data, String fileName, TypeReference<List<T>> typeRef) {
        List<T> existingDataList = new ArrayList<>();
        Path filePath = Path.of(fileName);
        log.info("Начало записи данных в файл {}", fileName);

        // Чтение существующего файла, если он есть
        if (Files.exists(filePath)) {
            try {
                existingDataList = mapper.readValue(filePath.toFile(), typeRef);
                log.info("Чтение существующих данных из файла {}", fileName);
            } catch (IOException e) {
                log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
            }
        }

        // Добавление новых данных и запись в файл
        existingDataList.addAll(data);
        try (Writer writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
            objectWriter.writeValue(writer, existingDataList);
            log.info("Данные успешно обновлены в файле {}", fileName);
        } catch (IOException e) {
            log.error("Ошибка записи в файл {}: {}", fileName, e.getMessage());
        }
        return existingDataList;
    }

    /**
     * Записывает список грузовиков в указанный JSON файл.
     *
     * @param trucks   список грузовиков, которые нужно записать
     * @param fileName путь к файлу
     * @return обновленный список грузовиков
     */
    public List<Truck> writeTrucks(List<Truck> trucks, String fileName) {
        return write(trucks, fileName, new TypeReference<>() {});
    }

    /**
     * Записывает список массивов char[][] в указанный JSON файл.
     *
     * @param parcels  список массивов char[][], которые нужно записать
     * @param fileName путь к файлу
     * @return обновленный список массивов char[][]
     */
    public List<char[][]> writeParcels(List<char[][]> parcels, String fileName) {
        return write(parcels, fileName, new TypeReference<>() {});
    }
}
