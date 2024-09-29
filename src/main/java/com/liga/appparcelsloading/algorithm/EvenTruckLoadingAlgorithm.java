package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.JsonFileWriter;
import com.liga.appparcelsloading.util.TruckWriter;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
@Service
public class EvenTruckLoadingAlgorithm implements TruckLoadAlgorithm {
    private static final int START_SIZE_PARCELS = 0;
    private final ParcelLoaderService parcelLoaderService;
    private final TruckCountValidate validateTruckCount;
    private final JsonFileWriter jsonFileWriter;
    private final TruckFactoryService truckFactoryService;

    public EvenTruckLoadingAlgorithm(TruckFactoryService truckFactoryService, ParcelLoaderService parcelLoaderService, TruckCountValidate validateTruckCount, JsonFileWriter jsonFileWriter) {
        this.truckFactoryService = truckFactoryService;
        this.parcelLoaderService = parcelLoaderService;
        this.validateTruckCount = validateTruckCount;
        this.jsonFileWriter = jsonFileWriter;
    }

    /**
     * Выполняет распределение посылок по грузовикам с использованием равномерного алгоритма загрузки.
     *
     * @param parcels    список посылок для распределения
     * @param countTruck количество грузовиков, доступных для загрузки
     * @param truckSize  размерность грузовика
     * @return список массивов символов, представляющих состояние грузовиков после загрузки
     */
    @Override
    public List<char[][]> loadParcels(List<Parcel> parcels, int countTruck, int truckSize) {
        log.info("Начало равномерного распределения {} посылок.", parcels.size());
        List<char[][]> trucks = new ArrayList<>();
        int numberTruck = 1;
        char[][] emptyTruck = truckFactoryService.createEmptyTruck(truckSize);

        final int sumParcels = getSumParcels(parcels);
        int maxLoading = getMaxLoading(countTruck, sumParcels);
        log.info("Максимальная загрузка одного грузовика: {}", maxLoading);

        trucks.add(getFullTruck(parcels, countTruck, maxLoading, emptyTruck, numberTruck, trucks, sumParcels, truckSize));
        if (validateTruckCount.validateTruckCount(countTruck, trucks)) {
            throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + trucks.size() + " грузовика(ов)");
        }
        log.info("Упаковка завершена. Количество грузовиков: {}", trucks.size());
        jsonFileWriter.writeParcels(trucks, "loading parcels.json");
        return trucks;
    }

    /**
     * Заполняет грузовики посылками до тех пор, пока не будет достигнута максимальная загрузка.
     * Если грузовик заполнен, создается новый грузовик, и оставшиеся посылки загружаются в него.
     *
     * @param parcels     список посылок для распределения
     * @param countTruck  количество грузовиков
     * @param maxLoading  максимальная допустимая загрузка одного грузовика
     * @param truck       текущий грузовик, который заполняется
     * @param numberTruck номер текущего грузовика
     * @param trucks      список всех грузовиков
     * @param sumParcels  общая сумма размеров всех посылок
     * @param truckSize   размерность грузовика
     * @return заполненный грузовик, если в него удалось поместить все посылки
     */
    private char[][] getFullTruck(List<Parcel> parcels, int countTruck, int maxLoading, char[][] truck, int numberTruck, List<char[][]> trucks, int sumParcels, int truckSize) {
        for (Parcel parcel : parcels) {
            int[][] parcelContent = parcel.getForm();
            char[][] symbolParcels = getSymbolParcels(parcel, parcelContent);
            maxLoading -= parcelContent[START_SIZE_PARCELS][START_SIZE_PARCELS];
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));

            if (maxLoading <= 0 || !parcelLoaderService.placeParcels(truck, symbolParcels, truckSize)) {
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                trucks.add(truck);
                truck = truckFactoryService.createEmptyTruck(truckSize);
                parcelLoaderService.placeParcels(truck, symbolParcels, truckSize);
                maxLoading = sumParcels / countTruck;
            }
            TruckWriter.getLoadingTrucks(numberTruck, parcelContent[0][0]);
        }
        return truck;
    }

    private static char[][] getSymbolParcels(Parcel parcel, int[][] parcelContent) {
        int numRows = parcelContent.length;
        int numCols = parcelContent[0].length;
        char[][] symbolParcels = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                symbolParcels[i][j] = parcel.getSymbol();
            }
        }
        return symbolParcels;
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
        return parcels.stream().mapToInt(parcel -> parcel.getForm()[START_SIZE_PARCELS][START_SIZE_PARCELS]).sum();
    }
}
