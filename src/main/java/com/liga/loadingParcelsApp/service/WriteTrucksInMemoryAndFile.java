package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Truck;
import com.liga.loadingParcelsApp.util.JsonFileWriter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для управления и записи информации о грузовиках в память и файл.
 * Хранит данные о грузовиках и их посылках, а также записывает эту информацию в файл в формате JSON.
 */

public class WriteTrucksInMemoryAndFile {
    @Getter
    private static final Map<Integer, List<Integer>> loadTrucks = new HashMap<>();
    private static List<Integer> parcels = new ArrayList<>();
    private static final JsonFileWriter JSON_FILE_WRITER = new JsonFileWriter();

    /**
     * Добавляет информацию о посылках для указанного грузовика.
     * Если грузовик еще не был зарегистрирован, создается новый список посылок.
     * Затем добавляет количество посылок в список и обновляет запись в карте грузовиков.
     *
     * @param numberTruck    номер грузовика
     * @param parcelsInTruck количество посылок в грузовике
     */
    public static void getLoadingTrucks(int numberTruck, int parcelsInTruck) {
        if (!loadTrucks.containsKey(numberTruck)) {
            parcels = new ArrayList<>();
        }
        parcels.add(parcelsInTruck);
        loadTrucks.put(numberTruck, parcels);
    }

    /**
     * Записывает информацию о грузовиках в файл в формате JSON.
     * Создает список объектов Truck на основе данных в карте загрузки грузовиков и записывает его в файл.
     */
    public static void writeTrucks(String fileName) {
        List<Truck> trucksList = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : loadTrucks.entrySet()) {
            Truck newTruck = new Truck("truck № " + entry.getKey(), entry.getValue());
            trucksList.add(newTruck);
        }
        JSON_FILE_WRITER.write(trucksList, fileName);
    }
}
