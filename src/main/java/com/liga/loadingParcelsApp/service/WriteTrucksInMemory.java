package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Truck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для управления и записи информации о грузовиках в память и файл.
 * Хранит данные о грузовиках и их посылках, а также записывает эту информацию в файл в формате JSON.
 */
public class WriteTrucksInMemory {
        private static final Map<Integer, List<Integer>> loadingTrucks = new HashMap<>();
        private static List<Integer> parcels = new ArrayList<>();

    /**
     * Добавляет информацию о посылках для указанного грузовика.
     * Если грузовик еще не был зарегистрирован, создается новый список посылок.
     * Затем добавляет количество посылок в список и обновляет запись в карте грузовиков.
     *
     * @param numberTruck номер грузовика
     * @param parcelsInTruck количество посылок в грузовике
     */
    public static void getLoadingTrucks(int numberTruck, int parcelsInTruck) {
        if (!loadingTrucks.containsKey(numberTruck)) {
            parcels = new ArrayList<>();
        }
        parcels.add(parcelsInTruck);
        loadingTrucks.put(numberTruck, parcels);
    }

    /**
     * Записывает информацию о грузовиках в файл в формате JSON.
     * Создает список объектов Truck на основе данных в карте загрузки грузовиков и записывает его в файл.
     */
    public static void writeTrucks() {
        List<Truck> trucksList = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : loadingTrucks.entrySet()) {
            Truck newTruck = new Truck("truck № " + entry.getKey(), entry.getValue());
            trucksList.add(newTruck);
        }
        JsonParser.writeListTrucksInFile(trucksList, "loading trucks.json");
    }
}
