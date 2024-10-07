package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;

import java.util.List;

/**
 * Интерфейс TruckLoadAlgorithm определяет методы для загрузки посылок в грузовики.
 * Содержит два метода: один для загрузки посылок на основе объектов посылок,
 * другой - на основе имен посылок.
 */
public interface TruckLoadAlgorithm {
    /**
     * Загружает список посылок в грузовики с учетом их размеров.
     *
     * @param parcels          список посылок для загрузки.
     * @param dimensionsTrucks список размеров грузовиков.
     * @return список грузовиков, загруженных посылками.
     */
    List<Truck> loadParcels(List<Parcel> parcels, List<Dimension> dimensionsTrucks);

    /**
     * Загружает посылки в грузовики на основе их имен.
     * Имена разделяются с помощью регулярного выражения и сопоставляются с объектами посылок.
     *
     * @param nameParcels      строка, содержащая имена посылок для загрузки.
     * @param dimensionsTrucks список размеров грузовиков.
     * @return список грузовиков, загруженных посылками.
     */
    List<Truck> loadParcelsByName(String nameParcels, List<Dimension> dimensionsTrucks);
}
