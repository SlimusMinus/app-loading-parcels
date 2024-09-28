package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.JsonFileWriter;

import java.util.List;

public abstract class TruckLoadAlgorithm {
    protected static final int TRUCK_SIZE = 6;
    protected final TruckFactoryService truckFactoryService;
    protected static final JsonFileWriter JSON_FILE_WRITER = new JsonFileWriter();

    protected TruckLoadAlgorithm(TruckFactoryService truckFactoryService) {
        this.truckFactoryService = truckFactoryService;
    }

    /**
     * Метод для загрузки посылок в грузовики.
     * @param parcels список посылок
     * @param countTruck количество грузовиков
     * @return список загруженных грузовиков
     */
    public abstract List<char[][]> loadParcels(List<Parcel> parcels, int countTruck);

    /**
     * Создаёт пустой грузовик.
     * @return пустой грузовик
     */
    protected char[][] createEmptyTruck() {
        return truckFactoryService.createEmptyTruck(TRUCK_SIZE);
    }
}
