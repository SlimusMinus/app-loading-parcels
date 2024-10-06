package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingRestAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingRestAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckRestAlgorithm;
import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.repository.TruckDataJpaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class TruckRestService {
    private final ParcelRepository repository;
    private final TruckDataJpaRepository truckDataJpaRepository;
    private final OptimalTruckLoadingRestAlgorithm optimalTruckLoadingAlgorithm;
    private final EvenTruckLoadingRestAlgorithm evenTruckLoadingAlgorithm;

    public Optional<List<Truck>> load(String algorithmType, String heights, String weights) {
        int[] heightArray = Arrays.stream(heights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] weightArray = Arrays.stream(weights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        TruckRestAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        List<Truck> trucks = algorithmLoadingParcels(truckLoadAlgorithm, heightArray, weightArray);
        for (Truck truck : trucks) {
            log.debug("Сохраняется грузовик: {}", truck);
            truckDataJpaRepository.save(truck);
        }
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(algorithmLoadingParcels(truckLoadAlgorithm, heightArray, weightArray));
    }

    public Optional<List<Truck>> loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        int[] heightArray = Arrays.stream(heights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] weightArray = Arrays.stream(weights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        TruckRestAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(algorithmLoadingParcelsByName(truckLoadAlgorithm, nameParcels, heightArray, weightArray));
    }

    private TruckRestAlgorithm getAlgorithm(String algorithmType) {
        return switch (algorithmType) {
            case "even" -> evenTruckLoadingAlgorithm;
            case "optimal" -> optimalTruckLoadingAlgorithm;
            default -> {
                log.error("Неизвестный тип алгоритма: {}", algorithmType);
                yield null;
            }
        };
    }

    private List<Truck> algorithmLoadingParcelsByName(TruckRestAlgorithm algorithm, String nameParcels, int[] height, int[] weight) {
        return algorithmLoadingParcels(algorithm, nameParcels, height, weight);
    }

    private List<Truck> algorithmLoadingParcels(TruckRestAlgorithm algorithm, int[] height, int[] weight) {
        return algorithmLoadingParcels(algorithm, null, height, weight);
    }


    private List<Truck> algorithmLoadingParcels(TruckRestAlgorithm truckLoadAlgorithm, String nameParcels, int[] height, int[] weight) {
        List<Dimension> allDimension = getAllDimension(height, weight);
        List<Truck> fullTrucks;
        if (nameParcels != null) {
            fullTrucks = truckLoadAlgorithm.loadParcelsByName(nameParcels, allDimension);
        } else {
            fullTrucks = truckLoadAlgorithm.loadParcels(getAllParcels(), allDimension);
        }
        log.info("Успешно упаковано {} грузовиков.", fullTrucks.size());
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

    private List<Parcel> getAllParcels() {
        return repository.findAll();
    }
}
