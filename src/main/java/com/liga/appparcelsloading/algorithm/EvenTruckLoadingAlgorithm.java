package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.FullTruck;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.util.TruckJsonWriter;
import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Класс, реализующий алгоритм равномерного распределения посылок по грузовикам.
 * <p>
 * Данный класс наследуется от {@link TruckLoadAlgorithm} и распределяет посылки равномерно по грузовикам
 * с учетом вместимости грузовиков. Основная логика заключается в том, что посылки загружаются в грузовики,
 * пока вместимость грузовика не будет исчерпана, после чего создается новый грузовик.
 * </p>
 */
@Slf4j
@Service
@AllArgsConstructor
public class EvenTruckLoadingAlgorithm implements TruckLoadAlgorithm {
    private static final int FIRST_INDEX = 0;
    private final ParcelLoaderService parcelLoaderService;
    private final TruckFactoryService truckFactoryService;
    private final ParcelMapper parcelMapper;
    private final TruckJsonWriter truckJsonWriter;
    /**
     * Основной метод для равномерного распределения посылок по грузовикам.
     * <p>
     * Загружает список посылок в грузовики, исходя из доступных размеров грузовиков.
     * </p>
     *
     * @param parcels          список посылок, которые нужно загрузить
     * @param dimensionsTrucks список размеров доступных грузовиков
     * @return список грузовиков, загруженных посылками
     */
    @Override
    public List<char[][]> loadParcels(List<Parcel> parcels, List<Dimension> dimensionsTrucks) {
        log.info("Начало равномерного распределения {} посылок.", parcels.size());
        List<char[][]> emptyTrucks = truckFactoryService.createEmptyTruck(dimensionsTrucks);

        int maxLoading = calculateMaxLoading(dimensionsTrucks, parcels);
        log.info("Максимальная загрузка одного грузовика: {}", maxLoading);

        List<char[][]> allFullTruck = getFullTruck(parcels, maxLoading, emptyTrucks);
        log.info("Упаковка завершена. Количество грузовиков: {}", allFullTruck.size());
        return allFullTruck;
    }

    /**
     * Метод для загрузки посылок по их именам.
     * <p>
     * Загружает посылки в грузовики, используя названия посылок, указанные в строке.
     * </p>
     *
     * @param nameParcels      строка с названиями посылок, разделёнными разделителями
     * @param dimensionsTrucks список размеров доступных грузовиков
     * @return список грузовиков, загруженных посылками
     */
    @Override
    public List<char[][]> loadParcelsByName(String nameParcels, List<Dimension> dimensionsTrucks) {
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

    private List<char[][]> getFullTruck(List<Parcel> parcels, int maxLoading, List<char[][]> emptyTrucks) {
        List<char[][]> trucks = new ArrayList<>();
        List<String> namesParcels = new ArrayList<>();
        List<FullTruck> fullFullTrucks = new ArrayList<>();
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
            namesParcels.add(parcel.getName());

            if (maxLoadingOneTruck <= 0 || !parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length)) {
                trucks.add(truck);
                log.info("Грузовик заполнен, создается новый грузовик.");
                numberTruck++;
                fullFullTrucks.add(new FullTruck("Truck № " + numberTruck, namesParcels, truck));
                counter++;
                if (counter < emptyTrucks.size()) {
                    truck = emptyTrucks.get(counter);
                    parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length);
                    maxLoadingOneTruck = maxLoading / emptyTrucks.size();
                } else if (counter >= emptyTrucks.size() + 1) {
                    throw new IllegalArgumentException("Не удалось загрузить посылки, необходимо " + counter + " грузовика(ов)");
                }
                namesParcels = new ArrayList<>();
            }
        }
        finalizedAddTruck(trucks, truck, numberTruck, fullFullTrucks, namesParcels);
        log.info("Количество загруженных грузовиков: {}", trucks.size());
        truckJsonWriter.write(fullFullTrucks, "loading truck.json");
        return trucks;
    }

    private static void finalizedAddTruck(List<char[][]> trucks, char[][] truck, int numberTruck, List<FullTruck> fullFullTrucks, List<String> namesParcels) {
        if (!trucks.contains(truck)) {
            trucks.add(truck);
            numberTruck++;
            fullFullTrucks.add(new FullTruck("Truck № " + numberTruck, namesParcels, truck));
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
