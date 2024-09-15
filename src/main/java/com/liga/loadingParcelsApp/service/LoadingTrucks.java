package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Package;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class LoadingTrucks {

    private static final int TRUCK_SIZE = 6;
    private static int numberTruck = 1;


    /**
     * Создает пустой грузовик размером 6x6, заполненный пробелами ' '.
     *
     * @return двумерный массив символов, представляющий пустой грузовик.
     */
    public char[][] createEmptyTruck() {
        log.trace("Создание пустого грузовика.");
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
     * <p>Метод также вызывает {@link WriteTrucksInMemory#getLoadingTrucks(int, int)} для сохранения информации
     * о номере грузовика и количестве посылок в этом грузовике. Это позволяет отслеживать распределение посылок
     * между грузовиками.</p>
     */
    public List<char[][]> packPackages(List<Package> parcels, int countTruck) {
        log.info("Начало упаковки {} посылок.", parcels.size());
        List<char[][]> trucks = new ArrayList<>();
        char[][] emptyTruck = createEmptyTruck();

        for (Package parcel : parcels) {
            int[][] parcelContent = parcel.getContent();
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));
            if (!placePackage(emptyTruck, parcelContent)) {
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                trucks.add(emptyTruck);
                emptyTruck = createEmptyTruck();
                placePackage(emptyTruck, parcelContent);
            }
            WriteTrucksInMemory.getLoadingTrucks(numberTruck, parcelContent[0][0]);
        }
        trucks.add(emptyTruck);
        log.info("Упаковка завершена. Количество грузовиков: {}", trucks.size());
        if (countTruck < trucks.size()) {
            throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + trucks.size() + " грузовика(ов)");
        }
        return trucks;
    }

    /**
     * Упаковывает посылки в грузовики с использованием алгоритма равномерного распределения.
     * Распределяет посылки по грузовикам, чтобы каждый грузовик имел приблизительно одинаковую загрузку.
     *
     * @param parcels    Список посылок, представленных двумерными массивами int.
     * @param countTruck Ожидаемое количество грузовиков.
     * @return Список упакованных грузовиков.
     * @throws IllegalArgumentException Если недостаточно грузовиков для размещения всех посылок.
     */
    public List<char[][]> evenlyDistributePackages(List<Package> parcels, int countTruck) {
        log.info("Начало равномерного распределения {} посылок.", parcels.size());
        List<char[][]> trucks = new ArrayList<>();
        char[][] emptyTruck = createEmptyTruck();

        List<int[][]> parcelQueue = new ArrayList<>(parcels.stream().map(Package::getContent).toList());
        int sumParcels = parcelQueue.stream().mapToInt(parcel -> parcel[0][0]).sum();
        int averageParcelSize = 5;
        int maxLoading = sumParcels / countTruck + averageParcelSize;
        log.info("Максимальная загрузка одного грузовика: {}", maxLoading);

        for (Package parcel : parcels) {
            int[][] parcelContent = parcel.getContent();
            maxLoading -= parcelContent[0][0];
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));
            if (maxLoading <= 0 || !placePackage(emptyTruck, parcelContent)) {
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                trucks.add(emptyTruck);
                emptyTruck = createEmptyTruck();
                placePackage(emptyTruck, parcelContent);
                maxLoading = sumParcels / countTruck;
            }
            WriteTrucksInMemory.getLoadingTrucks(numberTruck, parcelContent[0][0]);
        }
        trucks.add(emptyTruck);
        if (countTruck < trucks.size()) {
            throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + trucks.size() + " грузовика(ов)");
        }
        log.info("Упаковка завершена. Количество грузовиков: {}", trucks.size());
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
                    log.debug("Посылка размещена в грузовике по координатам: ({}, {})", i, j);
                    applyPackage(truck, parcel, i, j);
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
        log.trace("Посылка успешно размещена в грузовике.");
    }

    /**
     * Выводит на экран содержимое всех грузовиков.
     *
     * @param trucks Список грузовиков, каждый из которых представлен двумерным массивом символов.
     */
    public void printTrucks(List<char[][]> trucks) {
        log.info("Начало вывода содержимого {} грузовиков.", trucks.size());
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
        log.info("Вывод содержимого завершен.");
    }
}
