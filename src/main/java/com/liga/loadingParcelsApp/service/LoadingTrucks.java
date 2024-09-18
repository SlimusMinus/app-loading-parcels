package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Parcel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс {@code LoadingTrucks} отвечает за управление процессом загрузки посылок в грузовики.
 * Содержит два основных метода для упаковки посылок: равномерная загрузка и оптимальная загрузка.
 */
@Slf4j
public class LoadingTrucks {
    private final int TRUCK_SIZE = 6;
    private final int SIZE_PARCELS = 0;
    private final ParcelsLoader parcelsLoader = new ParcelsLoader();
    private final TruckFactory truckFactory = new TruckFactory();

    /**
     * Метод для оптимальной загрузки посылок в грузовики.
     * Загружает все посылки в минимальное количество грузовиков.
     *
     * @param parcels    список посылок для загрузки
     * @param countTruck количество грузовиков
     * @return список загруженных грузовиков
     */
    public List<char[][]> packParcels(List<Parcel> parcels, int countTruck) {
        log.info("Начало упаковки {} посылок.", parcels.size());
        List<char[][]> trucks = new ArrayList<>();
        int numberTruck = 1;
        char[][] emptyTruck = truckFactory.createEmptyTruck(TRUCK_SIZE);

        trucks.add(getFullTruck(parcels, countTruck, 0, emptyTruck, numberTruck, trucks, 0, false));
        log.info("Упаковка завершена. Количество грузовиков: {}", trucks.size());
        validateTruckCount(countTruck, trucks);
        return trucks;
    }

    /**
     * Метод для равномерной загрузки посылок по грузовикам.
     * Делит посылки поровну между грузовиками с учётом их размера.
     *
     * @param parcels    список посылок для загрузки
     * @param countTruck количество грузовиков
     * @return список загруженных грузовиков
     */
    public List<char[][]> evenlyPackParcels(List<Parcel> parcels, int countTruck) {
        log.info("Начало равномерного распределения {} посылок.", parcels.size());
        List<char[][]> trucks = new ArrayList<>();
        int numberTruck = 1;
        char[][] emptyTruck = truckFactory.createEmptyTruck(TRUCK_SIZE);

        final int sumParcels = getSumParcels(parcels);
        int maxLoading = getMaxLoading(countTruck, sumParcels);
        log.info("Максимальная загрузка одного грузовика: {}", maxLoading);

        trucks.add(getFullTruck(parcels, countTruck, maxLoading, emptyTruck, numberTruck, trucks, sumParcels, true));
        validateTruckCount(countTruck, trucks);
        log.info("Упаковка завершена. Количество грузовиков: {}", trucks.size());

        return trucks;
    }

    /**
     * Вспомогательный метод для загрузки посылок в грузовик.
     * Создает новый грузовик при необходимости и помещает посылки в грузовик.
     *
     * @param parcels       список посылок
     * @param countTruck    количество грузовиков
     * @param maxLoading    максимальная загрузка грузовика (используется при равномерной загрузке)
     * @param truck         текущий грузовик
     * @param numberTruck   номер текущего грузовика
     * @param trucks        список всех грузовиков
     * @param sumParcels    общее количество посылок
     * @param useMaxLoading использовать ли ограничение по загрузке
     * @return загруженный грузовик
     */
    private char[][] getFullTruck(List<Parcel> parcels, int countTruck, int maxLoading, char[][] truck, int numberTruck, List<char[][]> trucks, int sumParcels, boolean useMaxLoading) {
        for (Parcel parcel : parcels) {
            int[][] parcelContent = parcel.getContent();
            if (useMaxLoading) {
                maxLoading -= parcelContent[SIZE_PARCELS][SIZE_PARCELS];
            }
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));

            if ((useMaxLoading && maxLoading <= 0) || !parcelsLoader.placeParcels(truck, parcelContent, TRUCK_SIZE)) {
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                trucks.add(truck);
                truck = truckFactory.createEmptyTruck(TRUCK_SIZE);
                parcelsLoader.placeParcels(truck, parcelContent, TRUCK_SIZE);

                if (useMaxLoading) {
                    maxLoading = sumParcels / countTruck;
                }
            }
            WriteTrucksInMemoryAndFile.getLoadingTrucks(numberTruck, parcelContent[0][0]);
        }
        return truck;
    }

    /**
     * Проверяет, что количество грузовиков достаточно для загрузки всех посылок.
     *
     * @param countTruck запланированное количество грузовиков
     * @param trucks     фактическое количество загруженных грузовиков
     */
    private void validateTruckCount(int countTruck, List<char[][]> trucks) {
        if (countTruck < trucks.size()) {
            throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + trucks.size() + " грузовика(ов)");
        }
    }

    /**
     * Вычисляет максимальную загрузку одного грузовика при равномерной загрузке.
     *
     * @param countTruck количество грузовиков
     * @param sumParcels общее количество посылок
     * @return максимальная загрузка одного грузовика
     */
    private int getMaxLoading(int countTruck, int sumParcels) {
        final int AVERAGE_PARCELS_SIZE = 5;
        return sumParcels / countTruck + AVERAGE_PARCELS_SIZE;
    }

    /**
     * Вычисляет суммарный объём всех посылок.
     *
     * @param parcels список посылок
     * @return суммарный объём всех посылок
     */
    private int getSumParcels(List<Parcel> parcels) {
        List<int[][]> parcelQueue = new ArrayList<>(parcels.stream().map(Parcel::getContent).toList());
        return parcelQueue.stream().mapToInt(parcel -> parcel[SIZE_PARCELS][SIZE_PARCELS]).sum();
    }

}
