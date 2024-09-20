package com.liga.appparcelsloading;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.FileReader;
import com.liga.appparcelsloading.util.JsonFileReader;
import com.liga.appparcelsloading.util.JsonFileWriter;
import com.liga.appparcelsloading.util.TruckWriter;
import com.liga.appparcelsloading.validator.ParcelValidator;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Класс {@code TruckService} представляет собой сервис для управления процессом загрузки посылок в грузовики.
 * Он включает в себя метод для запуска процесса загрузки и взаимодействует с пользователем через консоль.
 * <p>
 * Класс использует {@link TruckLoadAlgorithm} для упаковки посылок и {@link FileReader} для чтения данных о посылках.
 * Также используется {@link ParcelValidator} для проверки валидности посылок и {@link JsonFileWriter} для чтения данных из файла JSON.
 * </p>
 */
@Slf4j
public class ManagerApp {
    private final List<Parcel> parcels;
    private final ParcelValidator parcelValidator;
    private final JsonFileReader jsonFileReader;
    private final TruckPrinterService truckPrinterService;
    private final ParcelLoaderService parcelLoaderService = new ParcelLoaderService();
    private final TruckFactoryService truckFactoryService = new TruckFactoryService();
    private final TruckCountValidate validateTruckCount = new TruckCountValidate();

    public ManagerApp(List<Parcel> parcels, ParcelValidator parcelValidator, JsonFileReader jsonFileReader, TruckPrinterService truckPrinterService) {
        this.parcels = parcels;
        this.parcelValidator = parcelValidator;
        this.jsonFileReader = jsonFileReader;
        this.truckPrinterService = truckPrinterService;
    }

    /**
     * Запускает процесс загрузки посылок и предоставляет пользователю выбор действий через консоль.
     * <p>
     * В этом методе выполняются следующие действия:
     * <ul>
     *     <li>Чтение списка посылок из файла "parcels.txt" с помощью {@link FileReader}.</li>
     *     <li>Проверка валидности посылок с использованием {@link ParcelValidator}.</li>
     *     <li>Предоставление пользователю меню для выбора алгоритма упаковки:</li>
     *     <ul>
     *         <li>1 - Равномерная загрузка по грузовикам (метод {@link TruckLoadAlgorithm#loadParcels(List, int)}).</li>
     *         <li>2 - Оптимальная загрузка (метод {@link TruckLoadAlgorithm#loadParcels(List, int)}).</li>
     *     </ul>
     *     <li>В зависимости от выбранного действия, выводится информация о загрузке грузовиков в консоль и сохраняются данные в память с помощью {@link TruckWriter}.</li>
     * </ul>
     * </p>
     */
    public void startLoading() {
        TruckLoadAlgorithm truckLoadService;
        log.info("Начало процесса загрузки посылок.");
        if (parcelValidator.isValid(parcels)) {
            log.info("Посылки прошли проверку на валидность.");
            do {
                System.out.println("""
                        Выберите действие:
                        1 - проверить алгоритм: равномерная погрузка по машинам
                        2 - проверить алгоритм: максимально качественная погрузка
                        3 - посмотреть сколько и какие посылки в машине из файла json
                        """);
                Scanner scanner = new Scanner(System.in);
                String choice = scanner.next();
                switch (choice) {
                    case "1":
                        truckLoadService = new EvenTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, validateTruckCount);
                        algorithmLoadingParcels(truckLoadService);
                        break;
                    case "2": {
                        truckLoadService = new OptimalTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, validateTruckCount);
                        algorithmLoadingParcels(truckLoadService);
                        break;
                    }
                    case "3": {
                        readJson();
                        break;
                    }
                    default:
                        System.exit(0);
                }
            } while (true);
        }
    }

    /**
     * Метод для выбора и выполнения алгоритма загрузки посылок в грузовики.
     * После упаковки выводит в консоль информацию о количестве грузовиков и сохраняет данные в JSON-файл.
     *
     * @param truckLoadService выбранный алгоритм загрузки {@link TruckLoadAlgorithm}
     */
    private void algorithmLoadingParcels(TruckLoadAlgorithm truckLoadService) {
        List<char[][]> trucks = truckLoadService.loadParcels(parcels, 4);
        log.info("Успешно упаковано {} грузовиков.", trucks.size());
        TruckWriter.writeTrucks("loading trucks.json");
        truckPrinterService.printTrucks(trucks);
    }

    /**
     * Метод для чтения данных о посылках в грузовиках из JSON-файла.
     * Для каждого грузовика выводит информацию о посылках и их размере.
     * Использует два файла JSON: один для списка грузовиков, другой для данных о посылках.
     */
    private void readJson() {
        final List<Truck> truckList = jsonFileReader.readTrucks("loading trucks.json");
        final List<char[][]> fullTrucks = jsonFileReader.readParcels("loading parcels.json");
        int fullTruckIndex = 0;
        for (Truck truck : truckList) {
            readParcels(fullTrucks, fullTruckIndex);
            fullTruckIndex++;
            readFullTruck(truck);
        }
    }

    /**
     * Выводит в консоль информацию о посылках в грузовике.
     * Определяет количество посылок каждого размера.
     *
     * @param truck объект Truck, содержащий информацию о грузовике и его посылках
     */
    private static void readFullTruck(Truck truck) {
        final int parcelIncrement = 1;
        System.out.println("Грузовик " + truck.getName() + " содержит");
        Map<Integer, Integer> integerMap = new HashMap<>();
        final List<Integer> truckParcels = truck.getParcels();
        for (Integer parcel : truckParcels) {
            integerMap.merge(parcel, parcelIncrement, Integer::sum);
        }
        integerMap.forEach((size, count) ->
                System.out.println(count + " посылки(у) размером " + size)
        );
    }

    /**
     * Выводит в консоль содержимое посылок в грузовике по его индексу.
     * Если индекс грузовика не превышает размер списка посылок, данные о посылках
     * выводятся с помощью сервиса {@code TruckPrinterService}.
     *
     * @param fullTrucks список массивов посылок (char[][]) для каждого грузовика
     * @param fullTruckIndex индекс текущего грузовика в списке
     */
    private void readParcels(List<char[][]> fullTrucks, int fullTruckIndex) {
        if (fullTruckIndex < fullTrucks.size()) {
            final char[][] fullTruck = fullTrucks.get(fullTruckIndex);
            truckPrinterService.printTrucks(Collections.singletonList(fullTruck));
        }
    }
}
