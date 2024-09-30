package com.liga.appparcelsloading.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Сервис для размещения посылок в грузовике.
 * Предоставляет методы для проверки возможности размещения посылок
 * и фактического размещения посылок в грузовике.
 */
@Slf4j
public class ParcelLoaderService {
    /**
     * Пытается разместить посылку в грузовике.
     *
     * @param truck  Грузовик, представленный двумерным массивом символов.
     * @param parcel Посылка, представленная двумерным массивом символов.
     * @param height Высота грузовика (количество строк).
     * @param width  Ширина грузовика (количество столбцов).
     * @return true, если посылка успешно размещена; false в противном случае.
     */
    public boolean placeParcels(char[][] truck, char[][] parcel, int height, int width) {
        // Перебор всех возможных позиций для размещения посылки в грузовике
        for (int i = height - parcel.length; i >= 0; i--) {
            final int SIZE_PARCELS = 0; // Используем для доступа к ширине посылки
            for (int j = 0; j <= width - parcel[SIZE_PARCELS].length; j++) {
                // Проверяем, можно ли разместить посылку на позиции (i, j)
                if (canPlace(truck, parcel, i, j)) {
                    log.debug("Посылка размещена в грузовике по координатам: ({}, {})", i, j);
                    applyParcels(truck, parcel, i, j); // Применяем размещение посылки
                    return true; // Посылка успешно размещена
                }
            }
        }
        log.warn("Нет места для посылки: {}", Arrays.deepToString(parcel));
        return false;
    }

    /**
     * Проверяет, можно ли разместить посылку в указанное место (координаты row, col) в грузовике.
     *
     * @param truck Грузовик, представленный двумерным массивом символов.
     * @param pack  Посылка, представленная двумерным массивом целых чисел.
     * @param row   Начальная строка для размещения посылки.
     * @param col   Начальный столбец для размещения посылки.
     * @return true, если посылку можно разместить; false, если место занято.
     */
    private boolean canPlace(char[][] truck, char[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            for (int j = 0; j < pack[i].length; j++) {
                if (truck[row + i][col + j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Размещает посылку в грузовике на заданные координаты (row, col).
     *
     * @param truck Грузовик, представленный двумерным массивом символов.
     * @param pack  Посылка, представленная двумерным массивом целых чисел.
     * @param row   Начальная строка для размещения посылки.
     * @param col   Начальный столбец для размещения посылки.
     */
    private void applyParcels(char[][] truck, char[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            System.arraycopy(pack[i], 0, truck[row + i], col, pack[i].length);
        }
        log.trace("Посылка успешно размещена в грузовике.");
    }
}
