package com.liga.loadingParcelsApp.util;

import com.liga.loadingParcelsApp.model.Truck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonFileReaderTest {
    private final JsonFileReader jsonFileReader = new JsonFileReader();

    @Test
    @DisplayName("Проверка чтения данных из JSON файла")
    void testRead() throws Exception {
        String jsonContent = """
            [
              {"name": "Truck 1", "parcels": [5, 10]},
              {"name": "Truck 2", "parcels": [7, 15]}
            ]
        """;
        String TEST_FILE = "test_trucks.json";
        Files.writeString(Path.of(TEST_FILE), jsonContent);
        List<Truck> trucks = jsonFileReader.read(TEST_FILE);

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getName()).isEqualTo("Truck 1");
        assertThat(trucks.get(0).getParcels()).containsExactly(5, 10);
        assertThat(trucks.get(1).getName()).isEqualTo("Truck 2");
        assertThat(trucks.get(1).getParcels()).containsExactly(7, 15);

        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    @DisplayName("Проверка обработки отсутствующего файла")
    void testReadFileNotFound() {
        List<Truck> trucks = jsonFileReader.read("non_existing_file.json");
        assertThat(trucks).isEmpty();
    }
}