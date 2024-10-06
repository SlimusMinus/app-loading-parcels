package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.JsonFileWriter;
import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Класс реализует алгоритм оптимальной загрузки грузовиков посылками.
 * Использует различные сервисы для создания пустых грузовиков, загрузки посылок и записи результатов в файл.
 */
@Slf4j
@Service
@AllArgsConstructor
public class OptimalTruckLoadingAlgorithm implements TruckLoadAlgorithm {
    private final ParcelLoaderService parcelLoaderService;
    private final TruckFactoryService truckFactoryService;
    private final ParcelMapper parcelMapper;
    private final JsonFileWriter jsonFileWriter;

    /**
     * Загружает посылки по их именам в грузовики, создаваемые на основе предоставленных размеров.
     *
     * @param nameParcels      строка с именами посылок, разделёнными запятыми, точками с запятой, пробелами и т.д.
     * @param dimensionsTrucks список размеров грузовиков
     * @return список массивов символов, представляющих полные грузовики с посылками
     */
    @Override
    public List<char[][]> loadParcelsByName(String nameParcels, List<Dimension> dimensionsTrucks) {
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

    /**
     * Загружает список посылок в грузовики, создаваемые на основе предоставленных размеров.
     *
     * @param parcels          список посылок для загрузки
     * @param dimensionsTrucks список размеров грузовиков
     * @return список массивов символов, представляющих полные грузовики с посылками
     */
    @Override
    public List<char[][]> loadParcels(List<Parcel> parcels, List<Dimension> dimensionsTrucks) {
        log.info("Начало упаковки {} посылок.", parcels.size());
        List<char[][]> emptyTrucks = truckFactoryService.createEmptyTruck(dimensionsTrucks);

        List<char[][]> allFullTruck = getFullTruck(parcels, emptyTrucks);
        log.info("Упаковка завершена. Количество грузовиков: {}", allFullTruck.size());

        return allFullTruck;
    }


    private List<char[][]> getFullTruck(List<Parcel> parcels, List<char[][]> emptyTrucks) {
        List<String> namesParcels = new ArrayList<>();
        List<Truck> trucks = new ArrayList<>();
        int numberTruck = 0;
        int counter = 0;
        char[][] truck = emptyTrucks.get(counter);

        for (Parcel parcel : parcels) {
            int[][] parcelContent = parcel.getForm();
            log.debug("Попытка разместить посылку: {}", Arrays.deepToString(parcelContent));
            char[][] symbolParcels = parcelMapper.getSymbolParcels(parcel, parcelContent);
            namesParcels.add(parcel.getName());
            if (!parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length)) {
                numberTruck++;
                log.info("Грузовик заполнен, создается новый грузовик.");
                trucks.add(new Truck("Truck № " + numberTruck, namesParcels, truck));
                counter++;
                if (counter < emptyTrucks.size()) {
                    truck = emptyTrucks.get(counter);
                    parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length);
                } else if (counter >= emptyTrucks.size() + 1) {
                    throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + counter + " грузовика(ов)");
                }                truck = emptyTrucks.get(counter);
                namesParcels = new ArrayList<>();
            }
        }

        if (!namesParcels.isEmpty()) {
            numberTruck++;
            trucks.add(new Truck("Truck № " + numberTruck, namesParcels, truck));
            log.info("Последний грузовик добавлен: Truck № {}", numberTruck);
        }

        jsonFileWriter.write(trucks, "loading truck.json");
        return emptyTrucks;
    }
}
