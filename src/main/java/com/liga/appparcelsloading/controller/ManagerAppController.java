package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.*;
import com.liga.appparcelsloading.validator.ParcelValidator;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

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
@ShellComponent
public class ManagerAppController {
    private final ParcelLoaderService parcelLoaderService;
    private final TruckFactoryService truckFactoryService;
    private final TruckCountValidate validateTruckCount;
    private final JsonFileReader jsonFileReader;
    private final TruckPrinterService truckPrinterService;
    private final JsonFileWriter jsonFileWriter;
    private final ParcelRepository repository;
    private final Scanner scanner;
    private final ParcelMapper parcelMapper;
    private final TruckWriter truckWriter;


    @ShellMethod(value = "Запуск процесса загрузки посылок.", key = "showMenu")
    public void showMenu() {
        System.out.println("""
                Выберите действие:
                load-evenly - Равномерная погрузка
                load-by-name-even - Погрузка по именам (равномерная)
                load-optimal - Максимально качественная погрузка
                load-by-name-optimal - Погрузка по именам (качественная)
                show-trucks - Показать содержимое грузовиков из JSON
                manage-parcels-menu - Редактировать посылки
                """);
    }

    @ShellMethod(value = "Равномерная загрузка посылок", key = "load-evenly")
    public void loadEvenly() {
        algorithmLoadingParcels(new EvenTruckLoadingAlgorithm(parcelLoaderService, jsonFileWriter, truckFactoryService, parcelMapper, truckWriter));
        showMenu();
    }

    @ShellMethod(value = "Загрузить посылки по именам (равномерно)", key = "load-by-name-even")
    public void loadByNameEven() {
        algorithmLoadingParcelsByName(new EvenTruckLoadingAlgorithm(parcelLoaderService, jsonFileWriter, truckFactoryService, parcelMapper, truckWriter));
        showMenu();
    }

    @ShellMethod(value = "Максимально качественная загрузка посылок", key = "load-optimal")
    public void loadOptimal() {
        algorithmLoadingParcels(new OptimalTruckLoadingAlgorithm(parcelLoaderService, validateTruckCount, jsonFileWriter, truckFactoryService, parcelMapper, truckWriter));
        showMenu();
    }

    @ShellMethod(value = "Загрузить посылки по именам (максимально качественно)", key = "load-by-name-optimal")
    public void loadByNameOptimal() {
        algorithmLoadingParcelsByName(new OptimalTruckLoadingAlgorithm(parcelLoaderService, validateTruckCount, jsonFileWriter, truckFactoryService, parcelMapper, truckWriter));
        showMenu();
    }

    @ShellMethod(value = "Показать содержимое грузовиков", key = "show-trucks")
    public void showTrucks() {
        readJson();
        showMenu();
    }

    private void algorithmLoadingParcelsByName(TruckLoadAlgorithm truckLoadAlgorithm) {
        System.out.println("Введите размерность грузовика");
        List<Dimension> allDimension = getAllDimension();
        System.out.println("Введите название посылок которые хотите погрузить");
        scanner.nextLine();
        String namesParcels = scanner.nextLine();
        List<char[][]> trucks = truckLoadAlgorithm.loadParcelsByName(namesParcels, allDimension);
        log.info("Успешно упаковано {} грузовиков.", trucks.size());
        truckWriter.writeTrucks("loading trucks.json");
        truckPrinterService.printTrucks(trucks);
    }

    private void algorithmLoadingParcels(TruckLoadAlgorithm truckLoadAlgorithm) {
        System.out.println("Введите размерность грузовика");
        List<Dimension> allDimension = getAllDimension();
        List<char[][]> trucks = truckLoadAlgorithm.loadParcels(getAllParcels(), allDimension);
        log.info("Успешно упаковано {} грузовиков.", trucks.size());
        truckWriter.writeTrucks("loading trucks.json");
        truckPrinterService.printTrucks(trucks);
    }

    private List<Dimension> getAllDimension() {
        List<Dimension> allDimension = new ArrayList<>();
        String choice;
        do {
            System.out.println("Введите высоту грузовика");
            int height = scanner.nextInt();
            System.out.println("Введите ширину грузовика");
            int width = scanner.nextInt();
            if (height < 5 || width < 5) {
                System.out.println("Размер грузовика должен быть больше 5");
            } else {
                allDimension.add(new Dimension(width, height));
            }
            System.out.println("Если вы еще хотите добавить грузовик введите 1");
            choice = scanner.next();
        } while (choice.equals("1"));
        return allDimension;
    }

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

    private void readParcels(List<char[][]> fullTrucks, int fullTruckIndex) {
        if (fullTruckIndex < fullTrucks.size()) {
            final char[][] fullTruck = fullTrucks.get(fullTruckIndex);
            truckPrinterService.printTrucks(Collections.singletonList(fullTruck));
        }
    }

    private List<Parcel> getAllParcels() {
        return repository.getAll();
    }
}
