package com.liga.appparcelsloading.util;

import com.liga.appparcelsloading.model.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("Тестирование класса FileReader")
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class FileReaderTest {

    @Autowired
    private FileReader fileReader;


    @Test
    @DisplayName("добавление 2 посылок с размером 9 и 6")
    void testGetAllParcels_validFile() throws IOException {
        Path tempFile = Files.createTempFile("testValidFile", ".txt");
        Files.write(tempFile, List.of(
                "999",
                "999",
                "999",
                "",
                "666",
                "666"
        ));
        List<Parcel> parcels = fileReader.getAllParcels(tempFile.toString());
        assertThat(2).isEqualTo(parcels.size());
        int[][] firstParcel = parcels.get(0).getForm();
        assertArrayEquals(new int[]{9, 9, 9}, firstParcel[0]);
        assertArrayEquals(new int[]{9, 9, 9}, firstParcel[1]);
        assertArrayEquals(new int[]{9, 9, 9}, firstParcel[2]);
        int[][] secondParcel = parcels.get(1).getForm();
        assertArrayEquals(new int[]{6, 6, 6}, secondParcel[0]);
        assertArrayEquals(new int[]{6, 6, 6}, secondParcel[1]);
        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("отработка пустых строк")
    void testGetAllParcels_emptyLines() throws IOException {
        Path tempFile = Files.createTempFile("testEmptyLines", ".txt");
        Files.write(tempFile, List.of(
                "55555",
                "",
                "",
                "333",
                ""
        ));
        List<Parcel> parcels = fileReader.getAllParcels(tempFile.toString());
        assertThat(2).isEqualTo(parcels.size());
        int[][] firstParcel = parcels.get(0).getForm();
        assertArrayEquals(new int[]{5, 5, 5, 5, 5}, firstParcel[0]);
        int[][] secondParcel = parcels.get(1).getForm();
        assertArrayEquals(new int[]{3, 3, 3}, secondParcel[0]);
        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("обработка несуществующего файла")
    void testGetAllParcels_invalidFile() {
        String invalidPath = "non_existent_file.txt";
        List<Parcel> parcels = fileReader.getAllParcels(invalidPath);
        assertThat(parcels.isEmpty()).isTrue();
    }

}