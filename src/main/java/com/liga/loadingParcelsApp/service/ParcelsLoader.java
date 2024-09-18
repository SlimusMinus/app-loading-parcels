package com.liga.loadingParcelsApp.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class ParcelsLoader {

    /**
     * Пытается разместить одну посылку в грузовике.
     *
     * @param truck  Грузовик, представленный двумерным массивом символов.
     * @param parcel Посылка, представленная двумерным массивом целых чисел.
     * @return true, если посылка успешно размещена; false, если нет места для посылки.
     */
    public boolean placeParcels(char[][] truck, int[][] parcel, int truck_size) {
        for (int i = truck_size - parcel.length; i >= 0; i--) {
            final int SIZE_PARCELS = 0;
            for (int j = 0; j <= truck_size - parcel[SIZE_PARCELS].length; j++) {
                if (canPlace(truck, parcel, i, j)) {
                    log.debug("Посылка размещена в грузовике по координатам: ({}, {})", i, j);
                    applyParcels(truck, parcel, i, j);
                    return true;
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
    private boolean canPlace(char[][] truck, int[][] pack, int row, int col) {
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
    private void applyParcels(char[][] truck, int[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            for (int j = 0; j < pack[i].length; j++) {
                truck[row + i][col + j] = (char) ('0' + pack[i][j]);
            }
        }
        log.trace("Посылка успешно размещена в грузовике.");
    }
}
