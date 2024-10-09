package com.liga.appparcelsloading.truck.service;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.truck.dto.TruckDto;
import com.liga.appparcelsloading.truck.mapper.TruckMapper;
import com.liga.appparcelsloading.truck.model.Dimension;
import com.liga.appparcelsloading.parcel.model.Parcel;
import com.liga.appparcelsloading.truck.model.Truck;
import com.liga.appparcelsloading.parcel.repository.ParcelRepository;
import com.liga.appparcelsloading.truck.repository.TruckDataJpaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления грузовиками, использующий REST API.
 * Предоставляет методы для загрузки грузовиков, поиска, удаления и получения списка грузовиков.
 */
@Slf4j
@AllArgsConstructor
@Service
public class TruckRestService {
    private final ParcelRepository repository;
    private final TruckDataJpaRepository truckDataJpaRepository;
    private final OptimalTruckLoadingAlgorithm optimalTruckLoadingAlgorithm;
    private final EvenTruckLoadingAlgorithm evenTruckLoadingAlgorithm;

    /**
     * Загружает грузовики с использованием указанного алгоритма, высот и ширин.
     *
     * @param algorithmType тип алгоритма для загрузки грузовиков (например, "even" или "optimal").
     * @param heights строки, содержащие высоты грузовиков, разделенные запятыми.
     * @param weights строки, содержащие ширины грузовиков, разделенные запятыми.
     * @return список грузовиков, загруженных с использованием заданного алгоритма, если алгоритм найден; иначе пустой {@link Optional}.
     */
    @Transactional
    public Optional<List<TruckDto>> load(String algorithmType, String heights, String weights) {
        int[] heightArray = getDimension(heights);
        int[] weightArray = getDimension(weights);
        TruckLoadAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        List<Truck> trucks = algorithmLoadingParcels(truckLoadAlgorithm, heightArray, weightArray);
        truckDataJpaRepository.saveAll(trucks);
        List<TruckDto> trucksDto = trucks.stream()
                .map(TruckMapper.INSTANCE::getTruckDto)
                .toList();
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(trucksDto);
    }

    /**
     * Загружает грузовики по названию посылок с использованием указанного алгоритма, высот и ширин.
     *
     * @param algorithmType тип алгоритма для загрузки грузовиков (например, "even" или "optimal").
     * @param nameParcels название посылок, которые нужно загрузить.
     * @param heights строки, содержащие высоты грузовиков, разделенные запятыми.
     * @param weights строки, содержащие ширины грузовиков, разделенные запятыми.
     * @return список грузовиков, загруженных с использованием заданного алгоритма, если алгоритм найден; иначе пустой {@link Optional}.
     */
    @Transactional
    public Optional<List<TruckDto>> loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        int[] heightArray = getDimension(heights);
        int[] weightArray = getDimension(weights);
        TruckLoadAlgorithm truckLoadAlgorithm = getAlgorithm(algorithmType);
        List<Truck> trucks = algorithmLoadingParcelsByName(truckLoadAlgorithm, nameParcels, heightArray, weightArray);
        truckDataJpaRepository.saveAll(trucks);
        List<TruckDto> trucksDto = trucks.stream()
                .map(TruckMapper.INSTANCE::getTruckDto)
                .toList();
        return truckLoadAlgorithm == null ? Optional.empty() : Optional.of(trucksDto);
    }

    /**
     * Возвращает все грузовики в виде списка {@link TruckDto}.
     *
     * @return список {@link TruckDto} с грузовиками. Если грузовиков нет, возвращает статус 204 (No Content).
     */
    public ResponseEntity<List<TruckDto>> findAll() {
        List<TruckDto> parcels = truckDataJpaRepository.findAll().stream().map(TruckMapper.INSTANCE::getTruckDto).toList();
        if (parcels.isEmpty()) {
            log.info("Список грузовиков пуст");
            return ResponseEntity.noContent().build();
        }
        log.info("Найдено {} грузовиков", parcels.size());
        return ResponseEntity.ok(parcels);
    }

    /**
     * Находит грузовик по заданному идентификатору.
     *
     * @param id идентификатор грузовика.
     * @return грузовик в виде {@link TruckDto}, если он найден; иначе статус 404 (Not Found).
     */
    public ResponseEntity<TruckDto> findById(int id) {
        Optional<Truck> parcel = truckDataJpaRepository.findById(id);
        if (parcel.isPresent()) {
            log.info("Найдена посылка с id '{}': {}", id, parcel.get());
            return ResponseEntity.ok(TruckMapper.INSTANCE.getTruckDto(parcel.get()));
        } else {
            log.warn("Посылка с '{}' id не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаляет грузовик по заданному идентификатору.
     *
     * @param id идентификатор грузовика для удаления.
     * @return статус 204 (No Content), если грузовик удален; иначе статус 404 (Not Found), если грузовик не найден.
     */
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

    private List<Truck> algorithmLoadingParcelsByName(TruckLoadAlgorithm algorithm, String nameParcels, int[] height, int[] weight) {
        return algorithmLoadingParcels(algorithm, nameParcels, height, weight);
    }

    private List<Truck> algorithmLoadingParcels(TruckLoadAlgorithm algorithm, int[] height, int[] weight) {
        return algorithmLoadingParcels(algorithm, null, height, weight);
    }


    private List<Truck> algorithmLoadingParcels(TruckLoadAlgorithm truckLoadAlgorithm, String nameParcels, int[] height, int[] weight) {
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
