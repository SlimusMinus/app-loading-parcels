package com.liga.appparcelsloading;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.ParcelService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.*;
import com.liga.appparcelsloading.validator.ParcelValidator;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

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
@AllArgsConstructor
@Service
public class ManagerApp implements CommandLineRunner {
    private final ParcelLoaderService parcelLoaderService;
    private final TruckFactoryService truckFactoryService;
    private final TruckCountValidate validateTruckCount;
    private final ParcelValidator parcelValidator;
    private final JsonFileReader jsonFileReader;
    private final TruckPrinterService truckPrinterService;
    private final JsonFileWriter jsonFileWriter;
    private final ParcelService parcelService;
    private final ParcelRepository repository;
    private final Scanner scanner;
    private final ParcelMapper parcelMapper;

    /**
     * Запускает процесс загрузки посылок и предоставляет пользователю выбор действий через консоль.
     * <p>
     * В этом методе выполняются следующие действия:
     * <ul>
     *     <li>Чтение списка посылок из файла "parcels.txt" с помощью {@link FileReader}.</li>
     *     <li>Проверка валидности посылок с использованием {@link ParcelValidator}.</li>
     *     <li>Предоставление пользователю меню для выбора алгоритма упаковки:</li>
     *     <ul>
     *         <li>1 - Равномерная загрузка по грузовикам (метод {@link TruckLoadAlgorithm#loadParcels(List, int, int)}).</li>
     *         <li>2 - Оптимальная загрузка (метод {@link TruckLoadAlgorithm#loadParcels(List, int, int)}).</li>
     *     </ul>
     *     <li>В зависимости от выбранного действия, выводится информация о загрузке грузовиков в консоль и сохраняются данные в память с помощью {@link TruckWriter}.</li>
     * </ul>
     * </p>
     */
    public void startLoading() {
        TruckLoadAlgorithm truckLoadService;
        log.info("Начало процесса загрузки посылок.");
        if (parcelValidator.isValid(getAllParcels())) {
            log.info("Посылки прошли проверку на валидность.");
            do {
                System.out.println("""
                        Выберите действие:
                        1 - проверить алгоритм: равномерная погрузка по машинам
                        2 - проверить алгоритм: максимально качественная погрузка
                        3 - посмотреть сколько и какие посылки в машине из файла json
                        4 - редактировать посылки
                        """);
                String choice = scanner.next();
                switch (choice) {
                    case "1":
                        truckLoadService = new EvenTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter);
                        algorithmLoadingParcels(truckLoadService);
                        break;
                    case "2": {
                        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter, parcelMapper);
                        algorithmLoadingParcels(truckLoadService);
                        break;
                    }
                    case "3": {
                        readJson();
                        break;
                    }
                    case "4": {
                        parcelService.parcelsManager();
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
        System.out.println("Введите размерность грузовика");
        int truckSize = scanner.nextInt();
        System.out.println("Введите количество грузиков для погрузки посылок");
        int countTruck = scanner.nextInt();
        List<char[][]> trucks = truckLoadService.loadParcels(getAllParcels(), countTruck, truckSize);
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
     * @param fullTrucks     список массивов посылок (char[][]) для каждого грузовика
     * @param fullTruckIndex индекс текущего грузовика в списке
     */
    private void readParcels(List<char[][]> fullTrucks, int fullTruckIndex) {
        if (fullTruckIndex < fullTrucks.size()) {
            final char[][] fullTruck = fullTrucks.get(fullTruckIndex);
            truckPrinterService.printTrucks(Collections.singletonList(fullTruck));
        }
    }

    private List<Parcel> getAllParcels(){
        return repository.getAll();
    }

    @Override
    public void run(String... args) throws Exception {
        startLoading();
    }
}
