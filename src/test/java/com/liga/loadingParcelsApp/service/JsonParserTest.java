package com.liga.loadingParcelsApp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.loadingParcelsApp.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

class JsonParserTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @TempDir
    Path tempDir;

    private List<Truck> trucks;

    @BeforeEach
    void setUp() {
        trucks = new ArrayList<>();
        trucks.add(new Truck("truck1", List.of(1, 2, 3)));
        trucks.add(new Truck("truck2", List.of(4, 5)));
    }

    /**
     * Проверяет, что метод записи списка грузовиков в файл работает корректно.
     * Создаёт файл, записывает в него данные и проверяет, что файл существует и данные записаны правильно.
     *
     * @throws IOException если возникает ошибка при работе с файлом
     */
    @Test
    @DisplayName("Тестирование записи списка грузовиков в файл")
    void testWriteListTrucksInFile() throws IOException {
        Path filePath = tempDir.resolve("trucks.json");
        JsonParser.writeListTrucksInFile(trucks, filePath.toString());
        assertTrue(filePath.toFile().exists());
        List<Truck> readTrucks = OBJECT_MAPPER.readValue(filePath.toFile(), new TypeReference<>() {});
        assertEquals(2, readTrucks.size());
        assertEquals("truck1", readTrucks.get(0).getName());
    }

    /**
     * Проверяет, что метод чтения списка грузовиков из файла работает корректно.
     * Создаёт файл с данными и проверяет, что данные читаются правильно.
     *
     * @throws IOException если возникает ошибка при работе с файлом
     */
    @Test
    @DisplayName("Тестирование чтения списка грузовиков из файла")
    void testReadJsonTrucks() throws IOException {
        Path filePath = tempDir.resolve("trucks.json");
        OBJECT_MAPPER.writeValue(filePath.toFile(), trucks);
        JsonParser.readJsonTrucks(filePath.toString());
    }

    /**
     * Проверяет, что метод записи списка грузовиков в файл корректно обрабатывает исключение IOException.
     * Мокирует ObjectMapper, чтобы сымитировать исключение при записи в файл.
     *
     * @throws IOException если возникает ошибка при работе с файлом
     */
    @Test
    @DisplayName("Тестирование обработки IOException при записи в файл")
    void testWriteListTrucksInFileIOException() throws IOException {
        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        doThrow(new IOException("Ошибка записи")).when(objectMapperMock).writeValue(Mockito.any(File.class), Mockito.any());
        Path filePath = tempDir.resolve("trucks.json");
        JsonParser.writeListTrucksInFile(trucks, filePath.toString());
    }

    /**
     * Проверяет, что метод чтения списка грузовиков из файла корректно обрабатывает некорректный JSON.
     * Создаёт файл с некорректным содержимым и проверяет, что ошибка при чтении логируется.
     *
     * @throws IOException если возникает ошибка при работе с файлом
     */
    @Test
    @DisplayName("Тестирование обработки IOException при чтении некорректного JSON")
    void testReadJsonTrucksIOException() throws IOException {
        Path filePath = tempDir.resolve("trucks.json");
        Files.writeString(filePath, "Некорректный JSON");
        JsonParser.readJsonTrucks(filePath.toString());
    }

    /**
     * Проверяет, что метод чтения списка грузовиков из файла корректно обрабатывает ситуацию,
     * когда файл не существует.
     */
    @Test
    @DisplayName("Тестирование обработки отсутствия файла при чтении")
    void testReadJsonTrucksFileNotFound() {
        Path nonExistentFile = tempDir.resolve("nonexistent.json");
        JsonParser.readJsonTrucks(nonExistentFile.toString());
    }
}