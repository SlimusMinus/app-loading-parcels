package com.liga.appparcelsloading.truck.service;

import com.liga.appparcelsloading.algorithm.*;
import com.liga.appparcelsloading.truck.model.Dimension;
import com.liga.appparcelsloading.truck.model.Truck;
import com.liga.appparcelsloading.parcel.model.Parcel;
import com.liga.appparcelsloading.parcel.repository.ParcelRepository;
import com.liga.appparcelsloading.util.JsonFileReader;
import com.liga.appparcelsloading.util.JsonFileWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис для управления грузовиками в памяти.
 * Предоставляет методы для загрузки посылок в грузовики,
 * отображения грузовиков и чтения данных из JSON-файлов.
 */
@Slf4j
@AllArgsConstructor
@Service
public class TruckInMemoryService {
    private final JsonFileReader jsonFileReader;
    private final TruckPrinterService truckPrinterService;
    private final ParcelRepository repository;
    private final EvenTruckLoadingAlgorithm evenTruckLoadingAlgorithm;
    private final OptimalTruckLoadingAlgorithm optimalTruckLoadingAlgorithm;
    private final JsonFileWriter jsonFileWriter;

    /**
     * Загружает посылки в грузовики на основе указанных алгоритма, высот и весов.
     *
     * @param algorithmType тип алгоритма загрузки (например, "even" или "optimal")
     * @param heights строка, содержащая высоты грузовиков, разделенные запятыми
     * @param weights строка, содержащая веса грузовиков, разделенные запятыми
     * @return список грузовиков с загруженными посылками, если загрузка прошла успешно;
     *         иначе пустой Optional
     */
    public Optional<List<char[][]>> load(String algorithmType, String heights, String weights) {
        int[] heightArray = getDimension(heights);
        int[] weightArray = getDimension(weights);
        TruckLoadAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(algorithmLoadingParcels(truckLoadAlgorithm, heightArray, weightArray));
    }

    /**
     * Загружает посылки в грузовики на основе указанного имени посылок,
     * алгоритма, высот и весов.
     *
     * @param algorithmType тип алгоритма загрузки (например, "even" или "optimal")
     * @param nameParcels имя посылок, которые необходимо загрузить
     * @param heights строка, содержащая высоты грузовиков, разделенные запятыми
     * @param weights строка, содержащая веса грузовиков, разделенные запятыми
     * @return список грузовиков с загруженными посылками, если загрузка прошла успешно;
     *         иначе пустой Optional
     */
    public Optional<List<char[][]>> loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        int[] heightArray = getDimension(heights);
        int[] weightArray = getDimension(weights);
        TruckLoadAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(algorithmLoadingParcelsByName(truckLoadAlgorithm, nameParcels, heightArray, weightArray));
    }

    /**
     * Отображает список всех грузовиков, загруженных из JSON-файла.
     *
     * @return список грузовиков
     */
    public List<Truck> showTrucks() {
        return readJson();
    }

    private static int[] getDimension(String heights) {
        return Arrays.stream(heights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
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
            fullTrucks = truckLoadAlgorithm.loadParcelsByName(nameParcels, allDimension).stream().map(Truck::getParcels).toList();
            jsonFileWriter.write(truckLoadAlgorithm.loadParcelsByName(nameParcels, allDimension), "loading truck.json");

        } else {
            fullTrucks = truckLoadAlgorithm.loadParcels(getAllParcels(), allDimension).stream().map(Truck::getParcels).toList();
            jsonFileWriter.write(truckLoadAlgorithm.loadParcels(getAllParcels(), allDimension), "loading truck.json");
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
        String nameParcels = truck.getNameParcels();
        System.out.println(nameParcels);
        truckPrinterService.printTrucks(Collections.singletonList(truck.getParcels()));
    }

    private List<Parcel> getAllParcels() {
        return repository.findAll();
    }
}
