package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Truck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteTrucksInMemory {
    private static final Map<Integer, List<Integer>> loadingTrucks = new HashMap<>();
    private static List<Integer> parcels = new ArrayList<>();

    public static void getLoadingTrucks(int numberTruck, int parcelsInTruck) {
        if (!loadingTrucks.containsKey(numberTruck)) {
            parcels = new ArrayList<>();
        }
        parcels.add(parcelsInTruck);
        loadingTrucks.put(numberTruck, parcels);
    }

    public static void writeTrucks() {
        List<Truck> trucksList = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : loadingTrucks.entrySet()) {
            Truck newTruck = new Truck("truck" + entry.getKey(), entry.getValue());
            trucksList.add(newTruck);
        }
        JsonWriterTrucks.writeListTrucksInFile(trucksList, "loading trucks.json");
    }
}
