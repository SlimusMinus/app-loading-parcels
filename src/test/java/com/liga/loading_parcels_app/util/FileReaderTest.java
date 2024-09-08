package com.liga.loading_parcels_app.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование класса FileReader")
class FileReaderTest {
    @Test
    @DisplayName("добавление 2 посылок с размером 9 и 6")
    void testGetAllPackages_validFile() throws IOException {
        Path tempFile = Files.createTempFile("testValidFile", ".txt");
        Files.write(tempFile, List.of(
                "999",
                "999",
                "999",
                "",
                "666",
                "666"
        ));
        List<int[][]> packages = FileReader.getAllPackages(tempFile.toString());
        assertThat(2).isEqualTo(packages.size());
        int[][] firstPackage = packages.get(0);
        assertArrayEquals(new int[]{9, 9, 9}, firstPackage[0]);
        assertArrayEquals(new int[]{9, 9, 9}, firstPackage[1]);
        assertArrayEquals(new int[]{9, 9, 9}, firstPackage[2]);
        int[][] secondPackage = packages.get(1);
        assertArrayEquals(new int[]{6, 6, 6}, secondPackage[0]);
        assertArrayEquals(new int[]{6, 6, 6}, secondPackage[1]);
        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("отработка пустых строк")
    void testGetAllPackages_emptyLines() throws IOException {
        Path tempFile = Files.createTempFile("testEmptyLines", ".txt");
        Files.write(tempFile, List.of(
                "55555",
                "",
                "",
                "333",
                ""
        ));
        List<int[][]> packages = FileReader.getAllPackages(tempFile.toString());
        assertThat(2).isEqualTo(packages.size());
        int[][] firstPackage = packages.get(0);
        assertArrayEquals(new int[]{5, 5, 5, 5, 5}, firstPackage[0]);
        int[][] secondPackage = packages.get(1);
        assertArrayEquals(new int[]{3, 3, 3}, secondPackage[0]);
        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("обработка несуществующего файла")
    void testGetAllPackages_invalidFile() {
        String invalidPath = "non_existent_file.txt";
        FileReader fileReaderMock = mock(FileReader.class);
        List<int[][]> packages = FileReader.getAllPackages(invalidPath);
        assertThat(packages.isEmpty()).isTrue();
        verify(fileReaderMock, times(1)).getAllPackages(invalidPath);
    }

}