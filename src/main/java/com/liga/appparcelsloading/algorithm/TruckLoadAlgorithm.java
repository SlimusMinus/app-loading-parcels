package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Parcel;

import java.util.List;

/**
 * Интерфейс для реализации алгоритмов загрузки посылок в грузовики.
 * Каждый алгоритм загрузки должен реализовать метод {@code loadParcels}, который распределяет посылки по грузовикам.
 */
public interface TruckLoadAlgorithm {
    /**
     * Распределяет список посылок по грузовикам согласно определенному алгоритму.
     *
     * @param parcels   список посылок, которые необходимо загрузить в грузовики
     * @param countTruck количество грузовиков, доступных для загрузки
     * @param truckSize  размер грузовика (вместимость), определяемая в виде двухмерного массива символов
     * @return список грузовиков, каждый из которых представлен двухмерным массивом символов,
     *         где каждая ячейка массива может быть пустой (' ') или содержать посылку
     */
    List<char[][]> loadParcels(List<Parcel> parcels, int countTruck, int truckSize);
}
