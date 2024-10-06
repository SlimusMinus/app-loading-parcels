package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.util.JsonFileReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class TruckService {
    private final JsonFileReader jsonFileReader;
    private final TruckPrinterService truckPrinterService;
    private final ParcelRepository repository;
    private final EvenTruckLoadingAlgorithm evenTruckLoadingAlgorithm;
    private final OptimalTruckLoadingAlgorithm optimalTruckLoadingAlgorithm;

    public Optional<List<char[][]>> load(String algorithmType, String heights, String weights) {
        int[] heightArray = Arrays.stream(heights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] weightArray = Arrays.stream(weights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        TruckLoadAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(algorithmLoadingParcels(truckLoadAlgorithm, heightArray, weightArray));
    }

    public Optional<List<char[][]>> loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        int[] heightArray = Arrays.stream(heights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] weightArray = Arrays.stream(weights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        TruckLoadAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(algorithmLoadingParcelsByName(truckLoadAlgorithm, nameParcels, heightArray, weightArray));
    }

    public List<Truck> showTrucks() {
        return readJson();
    }

    private TruckLoadAlgorithm getAlgorithm(String algorithmType) {
        return switch (algorithmType) {
            case "even" -> evenTruckLoadingAlgorithm;
            case "optimal" -> optimalTruckLoadingAlgorithm;
            default -> {
                log.error("Неизвестный тип алгоритма: {}", algorithmType);
                yield null;
            }
        };
    }

    private List<char[][]> algorithmLoadingParcelsByName(TruckLoadAlgorithm truckLoadAlgorithm, String nameParcels, int[] height, int[] weight) {
        return algorithmLoadingParcels(truckLoadAlgorithm, nameParcels, height, weight);
    }

    private List<char[][]> algorithmLoadingParcels(TruckLoadAlgorithm truckLoadAlgorithm, int[] height, int[] weight) {
        return algorithmLoadingParcels(truckLoadAlgorithm, null, height, weight);
    }


    private List<char[][]> algorithmLoadingParcels(TruckLoadAlgorithm truckLoadAlgorithm, String nameParcels, int[] height, int[] weight) {
        List<Dimension> allDimension = getAllDimension(height, weight);
        List<char[][]> fullTrucks;
        if (nameParcels != null) {
            fullTrucks = truckLoadAlgorithm.loadParcelsByName(nameParcels, allDimension);
        } else {
            fullTrucks = truckLoadAlgorithm.loadParcels(getAllParcels(), allDimension);
        }
        log.info("Успешно упаковано {} грузовиков.", fullTrucks.size());
        truckPrinterService.printTrucks(fullTrucks);
        return fullTrucks;
    }

    private List<Dimension> getAllDimension(int[] heights, int[] widths) {
        List<Dimension> allDimension = new ArrayList<>();
        for (int i = 0; i < heights.length; i++) {
            int height = heights[i];
            int width = widths[i];
            if (height < 5 || width < 5) {
                log.error("Размер грузовика должен быть больше 5: высота={}, ширина={}", height, width);
            } else {
                allDimension.add(new Dimension(width, height));
                log.info("Добавлен грузовик: высота={}, ширина={}", height, width);
            }
        }
        return allDimension;
    }

    private List<Truck> readJson() {
        final List<Truck> truckList = jsonFileReader.readTrucks("loading truck.json");
        for (Truck truck : truckList) {
            readFullTruck(truck);
        }
        return truckList;
    }

    private void readFullTruck(Truck truck) {
        log.info("Грузовик {} содержит", truck.getNameTruck());
        final List<String> truckParcels = truck.getNameParcels();
        for (String parcel : truckParcels) {
            System.out.println(parcel);
        }
        truckPrinterService.printTrucks(Collections.singletonList(truck.getParcels()));
    }

    private List<Parcel> getAllParcels() {
        return repository.findAll();
    }
}
