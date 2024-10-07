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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        int[] heightArray = getDimension(heights);
        int[] weightArray = getDimension(weights);
        TruckRestAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        List<Truck> trucks = algorithmLoadingParcels(truckLoadAlgorithm, heightArray, weightArray);
        truckDataJpaRepository.saveAll(trucks);
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(trucks);
    }

    public Optional<List<Truck>> loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        int[] heightArray = getDimension(heights);
        int[] weightArray = getDimension(weights);
        TruckRestAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        List<Truck> trucks = algorithmLoadingParcelsByName(truckLoadAlgorithm, nameParcels, heightArray, weightArray);
        truckDataJpaRepository.saveAll(trucks);
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(trucks);
    }

    public ResponseEntity<List<Truck>> findAll() {
        List<Truck> parcels = truckDataJpaRepository.findAll();
        if (parcels.isEmpty()) {
            log.info("Список грузовиков пуст");
            return ResponseEntity.noContent().build();
        }
        log.info("Найдено {} грузовиков", parcels.size());
        return ResponseEntity.ok(parcels);
    }

    public ResponseEntity<Truck> findById(int id) {
        Optional<Truck> parcel = truckDataJpaRepository.findById(id);
        if (parcel.isPresent()) {
            log.info("Найдена посылка с id '{}': {}", id, parcel.get());
            return ResponseEntity.ok(parcel.get());
        } else {
            log.warn("Посылка с '{}' id не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteById(int id) {
        int isDeleted = truckDataJpaRepository.deleteById(id);
        if (isDeleted != 0) {
            log.info("Посылка с ID {} удалена", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Посылка с ID {} не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }

    private static int[] getDimension(String heights) {
        return Arrays.stream(heights.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
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
