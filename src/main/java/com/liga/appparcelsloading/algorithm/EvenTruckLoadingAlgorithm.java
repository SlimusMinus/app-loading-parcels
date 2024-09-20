package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.TruckWriter;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс, реализующий алгоритм равномерного распределения посылок по грузовикам.
 * <p>
 * Данный класс наследуется от {@link TruckLoadAlgorithm} и распределяет посылки равномерно по грузовикам
 * с учетом вместимости грузовиков. Основная логика заключается в том, что посылки загружаются в грузовики,
 * пока вместимость грузовика не будет исчерпана, после чего создается новый грузовик.
 * </p>
 */
@Slf4j
public class EvenTruckLoadingAlgorithm extends TruckLoadAlgorithm {
    private static final int SIZE_PARCELS = 0;

    public EvenTruckLoadingAlgorithm(ParcelLoaderService parcelLoaderService, TruckFactoryService truckFactoryService, TruckCountValidate validateTruckCount) {
        super(parcelLoaderService, truckFactoryService, validateTruckCount);
    }

    /**
     * Выполняет распределение посылок по грузовикам с использованием равномерного алгоритма загрузки.
     *
     * @param parcels   список посылок для распределения
     * @param countTruck количество грузовиков, доступных для загрузки
     * @return список массивов символов, представляющих состояние грузовиков после загрузки
     */
    @Override
    public List<char[][]> loadParcels(List<Parcel> parcels, int countTruck) {
        log.info("Начало равномерного распределения {} посылок.", parcels.size());
        List<char[][]> trucks = new ArrayList<>();
        int numberTruck = 1;
        char[][] emptyTruck = createEmptyTruck();

        final int sumParcels = getSumParcels(parcels);
        int maxLoading = getMaxLoading(countTruck, sumParcels);
        log.info("Максимальная загрузка одного грузовика: {}", maxLoading);

        trucks.add(getFullTruck(parcels, countTruck, maxLoading, emptyTruck, numberTruck, trucks, sumParcels));
        if(validateTruckCount.validateTruckCount(countTruck, trucks)){
            throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + trucks.size() + " грузовика(ов)");
        }
        log.info("Упаковка завершена. Количество грузовиков: {}", trucks.size());
        JSON_FILE_WRITER.writeParcels(trucks, "loading parcels.json");
        return trucks;
    }

    /**
     * Заполняет грузовики посылками до тех пор, пока не будет достигнута максимальная загрузка.
     * Если грузовик заполнен, создается новый грузовик, и оставшиеся посылки загружаются в него.
     *
     * @param parcels      список посылок для распределения
     * @param countTruck   количество грузовиков
     * @param maxLoading   максимальная допустимая загрузка одного грузовика
     * @param truck        текущий грузовик, который заполняется
     * @param numberTruck  номер текущего грузовика
     * @param trucks       список всех грузовиков
     * @param sumParcels   общая сумма размеров всех посылок
     * @return заполненный грузовик, если в него удалось поместить все посылки
     */
    private char[][] getFullTruck(List<Parcel> parcels, int countTruck, int maxLoading, char[][] truck, int numberTruck, List<char[][]> trucks, int sumParcels) {
        for (Parcel parcel : parcels) {
            int[][] parcelContent = parcel.getContent();
            maxLoading -= parcelContent[SIZE_PARCELS][SIZE_PARCELS];
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));

            if (maxLoading <= 0 || !parcelLoaderService.placeParcels(truck, parcelContent, TRUCK_SIZE)) {
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                trucks.add(truck);
                truck = createEmptyTruck();
                parcelLoaderService.placeParcels(truck, parcelContent, TRUCK_SIZE);
                maxLoading = sumParcels / countTruck;
            }
            TruckWriter.getLoadingTrucks(numberTruck, parcelContent[0][0]);
        }
        return truck;
    }

    /**
     * Вычисляет максимальную загрузку одного грузовика на основе общего количества посылок и количества грузовиков.
     *
     * @param countTruck количество грузовиков
     * @param sumParcels общая сумма размеров всех посылок
     * @return максимальная загрузка одного грузовика
     */
    private int getMaxLoading(int countTruck, int sumParcels) {
        final int averageParcelsSize = 5;
        return sumParcels / countTruck + averageParcelsSize;
    }

    /**
     * Вычисляет общую сумму всех посылок.
     *
     * @param parcels список посылок
     * @return общая сумма размеров всех посылок
     */
    private int getSumParcels(List<Parcel> parcels) {
        return parcels.stream().mapToInt(parcel -> parcel.getContent()[SIZE_PARCELS][SIZE_PARCELS]).sum();
    }
}
