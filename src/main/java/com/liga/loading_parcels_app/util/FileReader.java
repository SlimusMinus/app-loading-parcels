package com.liga.loading_parcels_app.util;

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
    public static List<int[][]> getAllPackages(String fileName) {
        List<int[][]> packages = new ArrayList<>();
        Path filePath = Paths.get(fileName);
        try (Stream<String> lines = Files.lines(filePath)) {
            List<int[]> packageRows = new ArrayList<>();
            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (!line.isEmpty()) {
                    final int[] array = line.chars().map(Character::getNumericValue).toArray();
                    packageRows.add(array);
                }
                if (line.isEmpty() || !iterator.hasNext()) {
                    if (!packageRows.isEmpty()) {
                        packages.add(packageRows.toArray(new int[0][]));
                        packageRows = new ArrayList<>();
                    }
                }
            }
        } catch (IOException e) {
            log.error("Ошибка чтения файла по пути " + fileName + e.getMessage());
        }
        return packages;
    }
}
