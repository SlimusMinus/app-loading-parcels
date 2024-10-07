package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class OptimalTruckLoadingAlgorithm implements TruckLoadAlgorithm {
    private final ParcelLoaderService parcelLoaderService;
    private final TruckFactoryService truckFactoryService;
    private final ParcelMapper parcelMapper;

    @Override
    public List<Truck> loadParcelsByName(String nameParcels, List<Dimension> dimensionsTrucks) {
        log.info("Загрузка посылок по именам: {}", nameParcels);
        String[] splitNames = nameParcels.split("[,;: ]+");
        Map<String, Parcel> allParcels = parcelMapper.getAllParcels();

        List<Parcel> parcels = Arrays.stream(splitNames)
                .filter(allParcels::containsKey)
                .map(allParcels::get)
                .toList();
        log.info("Найдено {} посылок для загрузки.", parcels.size());
        return loadParcels(parcels, dimensionsTrucks);
    }

    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, List<Dimension> dimensionsTrucks) {
        log.info("Начало упаковки {} посылок.", parcels.size());
        List<char[][]> emptyTrucks = truckFactoryService.createEmptyTruck(dimensionsTrucks);

        List<Truck> allFullTruck = getFullTruck(parcels, emptyTrucks);
        log.info("Упаковка завершена. Количество грузовиков: {}", allFullTruck.size());

        return allFullTruck;
    }


    private List<Truck> getFullTruck(List<Parcel> parcels, List<char[][]> emptyTrucks) {
        StringBuilder namesParcels = new StringBuilder();
        List<Truck> trucks = new ArrayList<>();
        int numberTruck = 0;
        int counter = 0;
        char[][] truck = emptyTrucks.get(counter);

        for (Parcel parcel : parcels) {
            int[][] parcelContent = parcel.getForm();
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));
            char[][] symbolParcels = parcelMapper.getSymbolParcels(parcel, parcelContent);
            namesParcels.append(parcel.getName()).append(" ");
            if (!parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length)) {
                numberTruck++;
                log.info("Грузовик заполнен, создается новый грузовик.");
                trucks.add(new Truck("Truck № " + numberTruck, namesParcels.toString(), truck));
                counter++;
                if (counter < emptyTrucks.size()) {
                    truck = emptyTrucks.get(counter);
                    parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length);
                } else if (counter >= emptyTrucks.size() + 1) {
                    throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + counter + " грузовика(ов)");
                }
                truck = emptyTrucks.get(counter);
                namesParcels = new StringBuilder();
            }
        }

        if (!namesParcels.isEmpty()) {
            numberTruck++;
            trucks.add(new Truck("Truck № " + numberTruck, namesParcels.toString(), truck));
            log.info("Последний грузовик добавлен: Truck № {}", numberTruck);
        }
        return trucks;
    }

}
