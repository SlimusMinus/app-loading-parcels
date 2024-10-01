package com.liga.appparcelsloading.validator;

import com.liga.appparcelsloading.service.ParcelLoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

class TruckCountValidateTest {
    private TruckCountValidate truckCountValidate;
    private ParcelLoaderService parcelLoaderService;

    @BeforeEach
    void setUp() {
        truckCountValidate = new TruckCountValidate();
        parcelLoaderService = Mockito.mock(ParcelLoaderService.class);
    }

    @Test
    @DisplayName("Проверка успешной загрузки посылок в грузовик")
    void testValidationFullTruckSuccess() {
        List<char[][]> emptyTrucks = Arrays.asList(
                new char[][]{{' ', ' ', ' '}},
                new char[][]{{' ', ' ', ' '}}
        );
        char[][] symbolParcels = {{'+', '+'}, {'+', '+'}};
        int counter = 1;
        truckCountValidate.validationFullTruck(emptyTrucks, counter, symbolParcels, parcelLoaderService);
        verify(parcelLoaderService).placeParcels(emptyTrucks.get(counter), symbolParcels, emptyTrucks.get(counter).length, emptyTrucks.get(counter)[0].length);
    }

    @Test
    @DisplayName("Проверка ошибки при недостаточном количестве грузовиков")
    void testValidationFullTruckNotEnoughTrucks() {
        List<char[][]> emptyTrucks = new ArrayList<>();
        emptyTrucks.add(new char[][]{{' ', ' '}});
        char[][] symbolParcels = {{'+', '+'}};
        int counter = 2;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> truckCountValidate.validationFullTruck(emptyTrucks, counter, symbolParcels, parcelLoaderService)
                ).withMessageContaining("Не хватает грузовиков для загрузки посылок");
    }

    @Test
    @DisplayName("Проверка ошибки при необходимости 1 грузовика")
    void testValidationFullTruckRequiresOneTruck() {
        List<char[][]> emptyTrucks = new ArrayList<>();
        emptyTrucks.add(new char[][]{{' ', ' '}});
        char[][] symbolParcels = {{'+', '+'}};
        int counter = 1; // Запрашиваем один грузовик
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> truckCountValidate.validationFullTruck(emptyTrucks, counter, symbolParcels, parcelLoaderService))
                .withMessageContaining("Не удалось загрузить посылки, необходимо 1 грузовика(ов)");
    }
}