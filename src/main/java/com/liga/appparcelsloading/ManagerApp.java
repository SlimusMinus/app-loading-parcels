package com.liga.appparcelsloading;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.FileReader;
import com.liga.appparcelsloading.util.JsonFileReader;
import com.liga.appparcelsloading.util.JsonFileWriter;
import com.liga.appparcelsloading.util.TruckWriter;
import com.liga.appparcelsloading.validator.ParcelValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
    private TruckLoadAlgorithm truckLoadService;

    public ManagerApp() {
        FileReader fileReader = new FileReader();
        parcels = fileReader.getAllParcels("parcels.txt");
        parcelValidator = new ParcelValidator();
        jsonFileReader = new JsonFileReader();
        truckPrinterService = new TruckPrinterService();
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
                        truckLoadService = new EvenTruckLoadingAlgorithm();
                        algorithmLoadingParcels(truckLoadService);
                        break;
                    case "2": {
                        truckLoadService = new OptimalTruckLoadingAlgorithm();
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
     * Выводит в консоль информацию о каждом грузовике и количестве посылок определенного размера.
     */
    private void readJson() {
        final List<Truck> truckList = jsonFileReader.read("loading trucks.json");
        final int PARCEL_INCREMENT = 1;
        for (Truck truck : truckList) {
            System.out.println("Грузовик " + truck.getName() + " содержит");
            Map<Integer, Integer> parcels = new HashMap<>();
            final List<Integer> truckParcels = truck.getParcels();
            for (Integer parcel : truckParcels) {
                parcels.merge(parcel, PARCEL_INCREMENT, Integer::sum);
            }
            parcels.forEach((size, count) ->
                    System.out.println(count + " посылки(у) размером " + size)
            );
        }
    }
}
