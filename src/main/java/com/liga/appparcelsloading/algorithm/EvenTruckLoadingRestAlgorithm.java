package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class EvenTruckLoadingRestAlgorithm implements TruckRestAlgorithm{
    private static final int FIRST_INDEX = 0;
    private final ParcelLoaderService parcelLoaderService;
    private final TruckFactoryService truckFactoryService;
    private final ParcelMapper parcelMapper;
    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, List<Dimension> dimensionsTrucks) {
        log.info("Начало равномерного распределения {} посылок.", parcels.size());
        List<char[][]> emptyTrucks = truckFactoryService.createEmptyTruck(dimensionsTrucks);

        int maxLoading = calculateMaxLoading(dimensionsTrucks, parcels);
        log.info("Максимальная загрузка одного грузовика: {}", maxLoading);

        List<Truck> allFullTruck = getFullTruck(parcels, maxLoading, emptyTrucks);
        log.info("Упаковка завершена. Количество грузовиков: {}", allFullTruck.size());
        return allFullTruck;
    }

    @Override
    public List<Truck> loadParcelsByName(String nameParcels, List<Dimension> dimensionsTrucks) {
        log.info("Загрузка посылок по именам: {}", nameParcels);
        String delimiterRegex = "[,;: ]+";
        String[] splitNames = nameParcels.split(delimiterRegex);
        Map<String, Parcel> allParcels = parcelMapper.getAllParcels();
        List<Parcel> parcels = Arrays.stream(splitNames)
                .filter(allParcels::containsKey)
                .map(allParcels::get)
                .toList();
        log.info("Найдено {} посылок для загрузки.", parcels.size());
        return loadParcels(parcels, dimensionsTrucks);
    }

    private List<Truck> getFullTruck(List<Parcel> parcels, int maxLoading, List<char[][]> emptyTrucks) {
        List<char[][]> trucks = new ArrayList<>();
        StringBuilder namesParcels = new StringBuilder();
        List<Truck> fullTrucks = new ArrayList<>();
        int counter = 0;
        int numberTruck = 0;
        int maxLoadingOneTruck = maxLoading / emptyTrucks.size();
        char[][] truck = emptyTrucks.get(counter);
        log.debug("Первый грузовик создан с максимальной загрузкой: {}", maxLoadingOneTruck);
        for (Parcel parcel : parcels) {
            int[][] parcelContent = parcel.getForm();
            char[][] symbolParcels = parcelMapper.getSymbolParcels(parcel, parcel.getForm());
            maxLoadingOneTruck -= parcelContent[FIRST_INDEX][FIRST_INDEX];
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcel.getForm()));
            namesParcels.append(parcel.getName()).append(" ");

            if (maxLoadingOneTruck <= 0 || !parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length)) {
                trucks.add(truck);
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                fullTrucks.add(new Truck("Truck № " + numberTruck, namesParcels.toString(), truck));
                counter++;
                if (counter < emptyTrucks.size()) {
                    truck = emptyTrucks.get(counter);
                    parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length);
                    maxLoadingOneTruck = maxLoading / emptyTrucks.size();
                } else if (counter >= emptyTrucks.size() + 1) {
                    throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + counter + " грузовика(ов)");
                }
                namesParcels = new StringBuilder();
            }
        }
        finalizedAddTruck(trucks, truck, numberTruck, fullTrucks, namesParcels.toString());
        log.info("Количество загруженных грузовиков: {}", trucks.size());
        return fullTrucks;
    }

    private static void finalizedAddTruck(List<char[][]> trucks, char[][] truck, int numberTruck, List<Truck> fullTrucks, String namesParcels) {
        if (!trucks.contains(truck)) {
            trucks.add(truck);
            numberTruck++;
            fullTrucks.add(new Truck("Truck № " + numberTruck, namesParcels, truck));
            log.info("Последний грузовик добавлен: Truck № {}", numberTruck);
        }
    }

    private int calculateMaxLoading(List<Dimension> dimensions, List<Parcel> parcels) {
        int totalTruckArea = dimensions.stream()
                .mapToInt(dimension -> dimension.getWidth() * dimension.getHeight())
                .sum();
        int totalParcelArea = parcels.stream()
                .mapToInt(parcel -> Arrays.stream(parcel.getForm())
                        .mapToInt(arr -> Arrays.stream(arr).sum())
                        .sum())
                .sum();
        int averageTruckArea = totalTruckArea / dimensions.size();
        log.debug("Общая площадь грузовиков: {}", totalTruckArea);
        log.debug("Общая площадь посылок: {}", totalParcelArea);
        log.debug("Средняя площадь грузовиков: {}", averageTruckArea);
        return averageTruckArea < totalParcelArea ? averageTruckArea : totalParcelArea / dimensions.size();
    }
}
