package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.util.TruckWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckWriterTest {
    private File tempFile;
    private TruckWriter truckWriter;

    @BeforeEach
    void setUp() {
        tempFile = new File("test_trucks.json");
        truckWriter = new TruckWriter();
    }
    @Test
    @DisplayName("Проверка добавления информации о посылках в map грузовиков")
    void testGetLoadingTrucks() {
        truckWriter.getLoadingTrucks(1, 5);
        truckWriter.getLoadingTrucks(2, 7);
        assertThat(truckWriter.getLoadTrucksCopy().size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Проверка записи информации о грузовиках в JSON-файл")
    void testWriteTrucks() throws IOException {
        truckWriter.getLoadingTrucks(1, 5);
        truckWriter.getLoadingTrucks(2, 10);

        truckWriter.writeTrucks(tempFile.getAbsolutePath());
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
