package com.liga.appparcelsloading.util;

import com.liga.appparcelsloading.model.Parcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileReader {
    /**
     * Читает данные о посылках из файла и возвращает список объектов Parcels.
     *
     * @param fileName имя файла, из которого будут читаться данные
     * @return список объектов Parcels
     */
    public List<Parcel> getAllParcels(String fileName) {
        List<Parcel> parcels = new ArrayList<>();
        Path filePath = Paths.get(fileName);
        log.info("Начало чтения данных из файла: {}", fileName);

        try (Stream<String> lines = Files.lines(filePath)) {
            List<int[]> parcelRows = new ArrayList<>();
            Iterator<String> iterator = lines.iterator();

            while (iterator.hasNext()) {
                String line = iterator.next();
                log.debug("Чтение строки: {}", line);
                if (!line.isEmpty()) {
                    parcelRows.add(parseLine(line));
                }
                if (line.isEmpty() || !iterator.hasNext()) {
                    emptyRow(parcelRows, parcels);
                    log.debug("Добавлена посылка. Текущий размер списка посылок: {}", parcels.size());
                }
            }
            log.info("Успешно считано {} посылок из файла: {}", parcels.size(), fileName);
        } catch (IOException e) {
            log.error("Ошибка чтения файла по пути " + fileName + ": " + e.getMessage());
        }

        return parcels;
    }

    /**
     * Обрабатывает пустую строку, добавляя новую посылку в список {@link Parcel}, и очищает промежуточные данные.
     *
     * @param parcelRows промежуточные строки, представляющие посылку
     * @param parcels    список объектов {@link Parcel}, в который будет добавлена новая посылка
     */
    private void emptyRow(List<int[]> parcelRows, List<Parcel> parcels) {
        if (!parcelRows.isEmpty()) {
            parcels.add(createParcel(parcelRows));
            parcelRows.clear();
        }
    }

    /**
     * Преобразует строку в массив чисел.
     *
     * @param line строка для преобразования
     * @return массив чисел
     */
    private int[] parseLine(String line) {
        log.debug("Преобразование строки в массив чисел: {}", line);
        return line.chars().map(Character::getNumericValue).toArray();
    }

    /**
     * Создает объект Parcel из списка строк, представляющих посылку.
     *
     * @param parcelRows список строк, представляющих посылку
     * @return объект Parcel
     */
    private Parcel createParcel(List<int[]> parcelRows) {
        log.debug("Создание объекта Parcel из списка строк");
        int[][] content = parcelRows.toArray(new int[0][]);
        return new Parcel(content);
    }
}
