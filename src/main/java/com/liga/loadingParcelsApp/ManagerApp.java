package com.liga.loadingParcelsApp;

import com.liga.loadingParcelsApp.model.Parcel;
import com.liga.loadingParcelsApp.model.Truck;
import com.liga.loadingParcelsApp.service.LoadingTrucks;
import com.liga.loadingParcelsApp.service.TruckPrinter;
import com.liga.loadingParcelsApp.service.ParcelValidator;
import com.liga.loadingParcelsApp.service.WriteTrucksInMemoryAndFile;
import com.liga.loadingParcelsApp.util.FileReader;
import com.liga.loadingParcelsApp.util.JsonFileReader;
import com.liga.loadingParcelsApp.util.JsonFileWriter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Класс {@code TruckService} представляет собой сервис для управления процессом загрузки посылок в грузовики.
 * Он включает в себя метод для запуска процесса загрузки и взаимодействует с пользователем через консоль.
 * <p>
 * Класс использует {@link LoadingTrucks} для упаковки посылок и {@link FileReader} для чтения данных о посылках.
 * Также используется {@link ParcelValidator} для проверки валидности посылок и {@link JsonFileWriter} для чтения данных из файла JSON.
 * </p>
 */
@Slf4j
public class ManagerApp {
    /**
     * Запускает процесс загрузки посылок и предоставляет пользователю выбор действий через консоль.
     * <p>
     * В этом методе выполняются следующие действия:
     * <ul>
     *     <li>Чтение списка посылок из файла "parcels.txt" с помощью {@link FileReader}.</li>
     *     <li>Проверка валидности посылок с использованием {@link ParcelValidator}.</li>
     *     <li>Предоставление пользователю меню для выбора алгоритма упаковки:</li>
     *     <ul>
     *         <li>1 - Равномерная загрузка по грузовикам (метод {@link LoadingTrucks#evenlyPackParcels(List, int)}).</li>
     *         <li>2 - Оптимальная загрузка (метод {@link LoadingTrucks#packParcels(List, int)}).</li>
     *     </ul>
     *     <li>В зависимости от выбранного действия, выводится информация о загрузке грузовиков в консоль и сохраняются данные в память с помощью {@link WriteTrucksInMemoryAndFile}.</li>
     * </ul>
     * </p>
     */
    private final FileReader fileReader = new FileReader();
    private final List<Parcel> parcels = fileReader.getAllParcels("parcels.txt");
    private final ParcelValidator parcelValidator = new ParcelValidator();
    private final JsonFileReader jsonFileReader = new JsonFileReader();
    private final LoadingTrucks loadingTrucks = new LoadingTrucks();
    private final TruckPrinter truckPrinter = new TruckPrinter();

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
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        List<char[][]> trucks = loadingTrucks.evenlyPackParcels(parcels, 4);
                        log.info("Успешно упаковано {} грузовиков.", trucks.size());
                        WriteTrucksInMemoryAndFile.writeTrucks("loading trucks.json");
                        truckPrinter.printTrucks(trucks);
                        break;
                    case 2: {
                        List<char[][]> trucks2 = loadingTrucks.packParcels(parcels, 4);
                        log.info("Успешно упаковано {} грузовиков.", trucks2.size());
                        WriteTrucksInMemoryAndFile.writeTrucks("loading trucks.json");
                        truckPrinter.printTrucks(trucks2);
                        break;
                    }
                    case 3: {
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
                    default: System.exit(0);
                }
            } while (true);
        }
    }
}
