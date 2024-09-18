package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.validator.TruckCountValidate;

import java.util.List;

public abstract class TruckLoadAlgorithm {
    protected static final int TRUCK_SIZE = 6;
    protected static final int SIZE_PARCELS = 0;
    protected final ParcelLoaderService parcelLoaderService;
    protected final TruckFactoryService truckFactoryService;
    protected final TruckCountValidate validateTruckCount;

    public TruckLoadAlgorithm() {
        parcelLoaderService = new ParcelLoaderService();
        truckFactoryService = new TruckFactoryService();
        validateTruckCount = new TruckCountValidate();
    }

    /**
     * Метод для загрузки посылок в грузовики.
     * @param parcels список посылок
     * @param countTruck количество грузовиков
     * @return список загруженных грузовиков
     */
    public abstract List<char[][]> loadParcels(List<Parcel> parcels, int countTruck);

    /**
     * Проверяет, что количество грузовиков достаточно для загрузки всех посылок.
     * @param countTruck запланированное количество грузовиков
     * @param trucks фактическое количество загруженных грузовиков
     */
    protected void validateTruckCount(int countTruck, List<char[][]> trucks) {
        if (countTruck < trucks.size()) {
            throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + trucks.size() + " грузовика(ов)");
        }
    }

    /**
     * Создаёт пустой грузовик.
     * @return пустой грузовик
     */
    protected char[][] createEmptyTruck() {
        return truckFactoryService.createEmptyTruck(TRUCK_SIZE);
    }
}
