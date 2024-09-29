package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.JsonFileWriter;
import com.liga.appparcelsloading.util.TruckWriter;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс, реализующий алгоритм оптимальной загрузки посылок в грузовики.
 * <p>
 * Данный класс наследуется от {@link TruckLoadAlgorithm} и реализует алгоритм оптимальной загрузки,
 * который загружает посылки в грузовики, пока грузовик не заполнится, после чего создается новый грузовик.
 * </p>
 */
@Slf4j
public class OptimalTruckLoadingAlgorithm extends TruckLoadAlgorithm {
    private final ParcelLoaderService parcelLoaderService;
    private final TruckCountValidate validateTruckCount;
    private final JsonFileWriter jsonFileWriter;


    public OptimalTruckLoadingAlgorithm(TruckFactoryService truckFactoryService, ParcelLoaderService parcelLoaderService, TruckCountValidate validateTruckCount, JsonFileWriter jsonFileWriter) {
        super(truckFactoryService);
        this.parcelLoaderService = parcelLoaderService;
        this.validateTruckCount = validateTruckCount;
        this.jsonFileWriter = jsonFileWriter;
    }

    /**
     * Выполняет распределение посылок по грузовикам с использованием оптимального алгоритма загрузки.
     * Каждая посылка добавляется в грузовик до тех пор, пока не будет заполнена его вместимость.
     * Если грузовик заполняется, создается новый грузовик.
     *
     * @param parcels   список посылок для распределения
     * @param countTruck количество грузовиков, доступных для загрузки
     * @return список массивов символов, представляющих состояние грузовиков после загрузки
     */
    @Override
    public List<char[][]> loadParcels(List<Parcel> parcels, int countTruck) {
        log.info("Начало упаковки {} посылок.", parcels.size());
        List<char[][]> trucks = new ArrayList<>();
        int numberTruck = 1;
        char[][] emptyTruck = createEmptyTruck();

        trucks.add(getFullTruck(parcels, emptyTruck, numberTruck, trucks));
        log.info("Упаковка завершена. Количество грузовиков: {}", trucks.size());
        if(validateTruckCount.validateTruckCount(countTruck, trucks)){
            throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + trucks.size() + " грузовика(ов)");
        }
        jsonFileWriter.writeParcels(trucks, "loading parcels.json");
        return trucks;
    }

    /**
     * Заполняет грузовики посылками до тех пор, пока текущий грузовик не будет полностью загружен.
     * Если грузовик заполнен, создается новый грузовик, и оставшиеся посылки загружаются в него.
     *
     * @param parcels      список посылок для распределения
     * @param truck        текущий грузовик, который заполняется
     * @param numberTruck  номер текущего грузовика
     * @param trucks       список всех грузовиков
     * @return заполненный грузовик, если в него удалось поместить все посылки
     */
    private char[][] getFullTruck(List<Parcel> parcels, char[][] truck, int numberTruck, List<char[][]> trucks) {
        for (Parcel parcel : parcels) {
            int[][] parcelContent = parcel.getForm();
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));

            if (!parcelLoaderService.placeParcels(truck, parcelContent, TRUCK_SIZE)) {
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                trucks.add(truck);
                truck = createEmptyTruck();
                parcelLoaderService.placeParcels(truck, parcelContent, TRUCK_SIZE);
            }
            TruckWriter.getLoadingTrucks(numberTruck, parcelContent[0][0]);
        }
        return truck;
    }
}
