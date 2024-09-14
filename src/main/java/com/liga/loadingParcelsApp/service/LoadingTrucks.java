package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Package;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadingTrucks {

    private static final int TRUCK_SIZE = 6;

    /**
     * Создает пустой грузовик размером 6x6, заполненный пробелами ' '.
     *
     * @return двумерный массив символов, представляющий пустой грузовик.
     */
    public static char[][] createEmptyTruck() {
        char[][] emptyTruck = new char[TRUCK_SIZE][TRUCK_SIZE];
        for (int i = 0; i < TRUCK_SIZE; i++) {
            Arrays.fill(emptyTruck[i], ' ');
        }
        return emptyTruck;
    }

    /**
     * Принимает список посылок и пакует их в грузовики. Каждый грузовик имеет размер 6x6.
     * Если текущий грузовик заполнен, создается новый.
     *
     * @param parcels Список двумерных массивов типа int, где каждый массив представляет одну посылку.
     * @return список упакованных грузовиков, каждый из которых представлен двумерным массивом символов.
     */
    public List<char[][]> packPackages(List<Package> parcels) {
        List<char[][]> trucks = new ArrayList<>();
        char[][] emptyTruck = createEmptyTruck();

        for (Package parcel : parcels) {
            int[][] parcelContent = parcel.getContent();
            if (!placePackage(emptyTruck, parcelContent)) {
                trucks.add(emptyTruck);
                emptyTruck = createEmptyTruck();
                placePackage(emptyTruck, parcelContent);
            }
        }
        trucks.add(emptyTruck);
        return trucks;
    }

    /**
     * Пытается разместить одну посылку в грузовике.
     *
     * @param truck  Грузовик, представленный двумерным массивом символов.
     * @param parcel Посылка, представленная двумерным массивом целых чисел.
     * @return true, если посылка успешно размещена; false, если нет места для посылки.
     */
    public boolean placePackage(char[][] truck, int[][] parcel) {
        for (int i = TRUCK_SIZE - parcel.length; i >= 0; i--) {
            for (int j = 0; j <= TRUCK_SIZE - parcel[0].length; j++) {
                if (canPlace(truck, parcel, i, j)) {
                    applyPackage(truck, parcel, i, j);
                    return true;
                }
            }
        }
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
    public boolean canPlace(char[][] truck, int[][] pack, int row, int col) {
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
    public void applyPackage(char[][] truck, int[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            for (int j = 0; j < pack[i].length; j++) {
                truck[row + i][col + j] = (char) ('0' + pack[i][j]);
            }
        }
    }

    /**
     * Выводит на экран содержимое всех грузовиков.
     *
     * @param trucks Список грузовиков, каждый из которых представлен двумерным массивом символов.
     */
    public void printTrucks(List<char[][]> trucks) {
        System.out.println("++++++++");
        for (char[][] truck : trucks) {
            for (char[] row : truck) {
                System.out.print("+");
                for (char cell : row) {
                    System.out.print(cell);
                }
                System.out.print("+\n");
            }
            System.out.println("++++++++");
        }
    }
}
