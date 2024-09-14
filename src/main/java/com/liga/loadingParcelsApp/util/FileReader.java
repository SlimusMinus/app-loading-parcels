package com.liga.loadingParcelsApp.util;

import com.liga.loadingParcelsApp.model.Package;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class FileReader {
    /**
     * Читает данные о посылках из файла и возвращает список объектов Package.
     *
     * @param fileName имя файла, из которого будут читаться данные
     * @return список объектов Package
     */
    public List<Package> getAllPackages(String fileName) {
        List<Package> packages = new ArrayList<>();
        Path filePath = Paths.get(fileName);
        log.info("Начало чтения данных из файла: {}", fileName);

        try (Stream<String> lines = Files.lines(filePath)) {
            List<int[]> packageRows = new ArrayList<>();
            Iterator<String> iterator = lines.iterator();

            while (iterator.hasNext()) {
                String line = iterator.next();
                if (!line.isEmpty()) {
                    packageRows.add(parseLine(line));
                }
                if (line.isEmpty() || !iterator.hasNext()) {
                    emptyRow(packageRows, packages);
                }
            }
            log.info("Успешно считано {} посылок из файла: {}", packages.size(), fileName);
        } catch (IOException e) {
            log.error("Ошибка чтения файла по пути " + fileName + ": " + e.getMessage());
        }

        return packages;
    }

    private void emptyRow(List<int[]> packageRows, List<Package> packages) {
        if (!packageRows.isEmpty()) {
            packages.add(createPackage(packageRows));
            packageRows.clear();
        }
    }

    /**
     * Преобразует строку в массив чисел.
     *
     * @param line строка для преобразования
     * @return массив чисел
     */
    private int[] parseLine(String line) {
        return line.chars().map(Character::getNumericValue).toArray();
    }

    /**
     * Создает объект Package из списка строк, представляющих посылку.
     *
     * @param packageRows список строк, представляющих посылку
     * @return объект Package
     */
    private Package createPackage(List<int[]> packageRows) {
        int[][] content = packageRows.toArray(new int[0][]);
        return new Package(content);
    }
}
