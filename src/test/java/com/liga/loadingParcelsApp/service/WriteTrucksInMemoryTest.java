package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для класса {@link WriteTrucksInMemory}.
 * <p>
 * Этот класс тестов проверяет функциональность управления и записи информации о загрузке грузовиков,
 * чтобы убедиться, что внутреннее состояние загрузки грузовиков правильно отражается, и что вывод в формате JSON
 * создается корректно.
 */
public class WriteTrucksInMemoryTest {

    /**
     * Проверяет метод {@link WriteTrucksInMemory#getLoadingTrucks(int, int)}.
     * Убедитесь, что добавленные данные о посылках правильно сохраняются в статической map.
     *
     * @throws NoSuchFieldException   если не удается найти поле
     * @throws IllegalAccessException если доступ к полю запрещен
     */
    @Test
    @DisplayName("Проверка добавления информации о посылках в карту грузовиков")
    void testGetLoadingTrucks() throws NoSuchFieldException, IllegalAccessException {
        WriteTrucksInMemory.getLoadingTrucks(1, 5);
        WriteTrucksInMemory.getLoadingTrucks(1, 10);
        Map<Integer, List<Integer>> expectedMap = Map.of(
                1, List.of(5, 10)
        );
        Field field = WriteTrucksInMemory.class.getDeclaredField("loadingTrucks");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, List<Integer>> actualMap = (Map<Integer, List<Integer>>) field.get(null);
        assertThat(expectedMap).isEqualTo(actualMap);
    }
}
