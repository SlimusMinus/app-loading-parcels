package com.liga.appparcelsloading.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.model.FullTruck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonFileReaderTest {
    private final JsonFileReader jsonFileReader = new JsonFileReader(new ObjectMapper());

    @Test
    @DisplayName("Проверка чтения данных из JSON файла")
    void testRead() throws Exception {
        String jsonContent = """
                [
                    {
                        "nameTruck": "Truck № 2",
                        "nameParcels": [
                            "Холодильник",
                            "Телевизор",
                            "Комбайн"
                        ],
                        "parcels": [
                            ["+", "+", "+", " ", " "],
                            ["+", "+", "+", " ", " "],
                            ["+", "+", "+", " ", " "],
                            ["*", "*", "*", "*", " "],
                            ["*", "*", "*", "*", " "],
                            ["$", "$", "$", "$", " "]
                        ]
                    },
                    {
                        "nameTruck": "Truck № 3",
                        "nameParcels": [
                            "Пылесос",
                            "Телефон"
                        ],
                        "parcels": [
                            [" ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " "],
                            [" ", "&", "&", "&", "^", "^", "^"],
                            ["#", "#", "&", "&", "^", "^", "^"]
                        ]
                    }
                ]""";

        String TEST_FILE = "test_trucks.json";
        Files.writeString(Path.of(TEST_FILE), jsonContent);

        List<FullTruck> trucks = jsonFileReader.readTrucks(TEST_FILE);

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getNameTruck()).isEqualTo("Truck № 2");
        assertThat(trucks.get(1).getNameTruck()).isEqualTo("Truck № 3");

        assertThat(trucks.get(0).getParcels()[0]).containsExactly('+', '+', '+', ' ', ' ');

        assertThat(trucks.get(1).getParcels()[6]).containsExactly(' ', '&', '&', '&', '^', '^', '^');

        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    @DisplayName("Проверка обработки отсутствующего файла")
    void testReadFileNotFound() {
        List<FullTruck> trucks = jsonFileReader.readTrucks("non_existing_file.json");
        assertThat(trucks).isEmpty();
    }
}
