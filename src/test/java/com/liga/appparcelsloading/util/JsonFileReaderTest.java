package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.model.FullTruck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonFileReaderTest {
    private final JsonFileReader jsonFileReader = new JsonFileReader(new ObjectMapper());

    @Test
    @DisplayName("Проверка чтения данных из JSON файла")
    void testRead() throws Exception {
        String TEST_FILE = "test_trucks.json";
        Path jsonFilePath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(TEST_FILE)).toURI());

        List<FullTruck> trucks = jsonFileReader.readTrucks(jsonFilePath.toString());

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getNameTruck()).isEqualTo("Truck № 2");
        assertThat(trucks.get(1).getNameTruck()).isEqualTo("Truck № 3");

        assertThat(trucks.get(0).getParcels()[0]).containsExactly('+', '+', '+', ' ', ' ');

        assertThat(trucks.get(1).getParcels()[6]).containsExactly(' ', '&', '&', '&', '^', '^', '^');
    }

    @Test
    @DisplayName("Проверка обработки отсутствующего файла")
    void testReadFileNotFound() {
        List<FullTruck> trucks = jsonFileReader.readTrucks("non_existing_file.json");
        assertThat(trucks).isEmpty();
    }
}
