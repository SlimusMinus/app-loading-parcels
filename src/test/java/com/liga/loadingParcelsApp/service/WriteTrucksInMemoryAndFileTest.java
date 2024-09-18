package com.liga.loadingParcelsApp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class WriteTrucksInMemoryAndFileTest {
    private File tempFile;

    @BeforeEach
    void setUp() {
        tempFile = new File("test_trucks.json");
    }
    @Test
    @DisplayName("Проверка добавления информации о посылках в map грузовиков")
    void testGetLoadingTrucks() {
        WriteTrucksInMemoryAndFile.getLoadingTrucks(1, 5);
        WriteTrucksInMemoryAndFile.getLoadingTrucks(2, 7);
        assertThat(WriteTrucksInMemoryAndFile.getLoadTrucks().size()).isEqualTo(2);
        assertThat(WriteTrucksInMemoryAndFile.getLoadTrucks().get(1)).containsExactly(5);
        assertThat(WriteTrucksInMemoryAndFile.getLoadTrucks().get(2)).containsExactly(7);
    }

    @Test
    @DisplayName("Проверка записи информации о грузовиках в JSON-файл")
    void testWriteTrucks() throws IOException {
        WriteTrucksInMemoryAndFile.getLoadingTrucks(1, 5);
        WriteTrucksInMemoryAndFile.getLoadingTrucks(2, 10);

        WriteTrucksInMemoryAndFile.writeTrucks(tempFile.getAbsolutePath());
        assertThat(tempFile.exists()).isTrue();
        String fileContent = Files.readString(Path.of(tempFile.getAbsolutePath()));

        assertThat(fileContent).contains("truck № 1");
        assertThat(fileContent).contains("truck № 2");
        assertThat(fileContent).contains("5");
        assertThat(fileContent).contains("10");
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}
