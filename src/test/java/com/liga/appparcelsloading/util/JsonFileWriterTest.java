package com.liga.appparcelsloading.util;

import com.liga.appparcelsloading.model.Truck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonFileWriterTest {
    private JsonFileWriter jsonFileWriter;
    private static final String TEST_FILE = "test_trucks.json";

    @BeforeEach
    void setUp() {
        jsonFileWriter = new JsonFileWriter();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    @DisplayName("Проверка записи списка грузовиков в новый файл")
    void testWriteNewFile() {
        List<Truck> trucks = List.of(
                new Truck("Truck 1", List.of(5, 10)),
                new Truck("Truck 2", List.of(7, 15))
        );

        List<Truck> result = jsonFileWriter.write(trucks, TEST_FILE);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Truck 1");
        assertThat(result.get(1).getName()).isEqualTo("Truck 2");
        assertThat(Files.exists(Paths.get(TEST_FILE))).isTrue();
    }

    @Test
    @DisplayName("Проверка обновления существующего файла грузовиками")
    void testWriteExistingFile() {
        List<Truck> initialTrucks = List.of(
                new Truck("Truck 1", List.of(5, 10))
        );
        List<Truck> newTrucks = List.of(
                new Truck("Truck 2", List.of(7, 15))
        );

        jsonFileWriter.write(initialTrucks, TEST_FILE);
        List<Truck> result = jsonFileWriter.write(newTrucks, TEST_FILE);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Truck 1");
        assertThat(result.get(1).getName()).isEqualTo("Truck 2");
    }

    @Test
    @DisplayName("Проверка записи пустого списка грузовиков")
    void testWriteEmptyList() {
        List<Truck> result = jsonFileWriter.write(Collections.emptyList(), TEST_FILE);
        assertThat(result).isEmpty();
        assertThat(Files.exists(Paths.get(TEST_FILE))).isTrue();
    }
}
