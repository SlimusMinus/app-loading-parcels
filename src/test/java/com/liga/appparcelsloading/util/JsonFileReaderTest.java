package com.liga.appparcelsloading.util;

import com.liga.appparcelsloading.model.Truck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
public class JsonFileReaderTest {
    @Autowired
    private JsonFileReader jsonFileReader;

    @Test
    @DisplayName("Проверка чтения данных из JSON файла")
    void testRead() throws Exception {
        String TEST_FILE = "test_trucks.json";
        Path jsonFilePath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(TEST_FILE)).toURI());

        List<Truck> trucks = jsonFileReader.readTrucks(jsonFilePath.toString());

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getNameTruck()).isEqualTo("Truck № 2");
        assertThat(trucks.get(1).getNameTruck()).isEqualTo("Truck № 3");

        assertThat(trucks.get(0).getParcels()[0]).containsExactly('+', '+', '+', ' ', ' ');

        assertThat(trucks.get(1).getParcels()[6]).containsExactly(' ', '&', '&', '&', '^', '^', '^');
    }

    @Test
    @DisplayName("Проверка обработки отсутствующего файла")
    void testReadFileNotFound() {
        List<Truck> trucks = jsonFileReader.readTrucks("non_existing_file.json");
        assertThat(trucks).isEmpty();
    }
}
