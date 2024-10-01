package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.FullTruck;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.FileReader;
import com.liga.appparcelsloading.util.TruckJsonWriter;
import com.liga.appparcelsloading.util.JsonFileReader;
import com.liga.appparcelsloading.util.ParcelMapper;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Класс {@code TruckService} представляет собой сервис для управления процессом загрузки посылок в грузовики.
 * Он включает в себя метод для запуска процесса загрузки и взаимодействует с пользователем через консоль.
 * <p>
 * Класс использует {@link TruckLoadAlgorithm} для упаковки посылок и {@link FileReader} для чтения данных о посылках.
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
    private final ParcelRepository repository;
    private final Scanner scanner;
    private final ParcelMapper parcelMapper;
    private final TruckJsonWriter truckJsonWriter;

    @ShellMethod(value = "Запуск процесса загрузки посылок.", key = "showMenu")
    public void showMenu() {
        System.out.print("""
                Выберите действие:
                load-evenly - Равномерная погрузка
                load-by-name-even - Погрузка по именам (равномерная)
                load-optimal - Максимально качественная погрузка
                load-by-name-optimal - Погрузка по именам (качественная)
                show-fullTrucks - Показать содержимое грузовиков из JSON
                manage-parcels-menu - Редактировать посылки
                """);
    }

    @ShellMethod(value = "Равномерная загрузка посылок", key = "load-evenly")
    public void loadEvenly() {
        algorithmLoadingParcels(new EvenTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, parcelMapper, truckJsonWriter));
        showMenu();
    }

    @ShellMethod(value = "Загрузить посылки по именам (равномерно)", key = "load-by-name-even")
    public void loadByNameEven() {
        algorithmLoadingParcelsByName(new EvenTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, parcelMapper, truckJsonWriter));
        showMenu();
    }

    @ShellMethod(value = "Максимально качественная загрузка посылок", key = "load-optimal")
    public void loadOptimal() {
        algorithmLoadingParcels(new OptimalTruckLoadingAlgorithm(parcelLoaderService, validateTruckCount, truckFactoryService, parcelMapper, truckJsonWriter));
        showMenu();
    }

    @ShellMethod(value = "Загрузить посылки по именам (максимально качественно)", key = "load-by-name-optimal")
    public void loadByNameOptimal() {
        algorithmLoadingParcelsByName(new OptimalTruckLoadingAlgorithm(parcelLoaderService, validateTruckCount, truckFactoryService, parcelMapper, truckJsonWriter));
        showMenu();
    }

    @ShellMethod(value = "Показать содержимое грузовиков", key = "show-fullTrucks")
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
        List<char[][]> fullTrucks = truckLoadAlgorithm.loadParcelsByName(namesParcels, allDimension);
        log.info("Успешно упаковано {} грузовиков.", fullTrucks.size());
        truckPrinterService.printTrucks(fullTrucks);
    }

    private void algorithmLoadingParcels(TruckLoadAlgorithm truckLoadAlgorithm) {
        System.out.println("Введите размерность грузовика");
        List<Dimension> allDimension = getAllDimension();
        List<char[][]> fullTrucks = truckLoadAlgorithm.loadParcels(getAllParcels(), allDimension);
        log.info("Успешно упаковано {} грузовиков.", fullTrucks.size());
        truckPrinterService.printTrucks(fullTrucks);
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
        final List<FullTruck> fullTruckList = jsonFileReader.readTrucks("loading truck.json");
        for (FullTruck fullTruck : fullTruckList) {
            readFullTruck(fullTruck);
        }
    }

    private void readFullTruck(FullTruck fullTruck) {
        System.out.println("Грузовик " + fullTruck.getNameTruck() + " содержит");
        final List<String> truckParcels = fullTruck.getNameParcels();
        for (String parcel : truckParcels) {
            System.out.println(parcel);
        }
        truckPrinterService.printTrucks(Collections.singletonList(fullTruck.getParcels()));
    }

    private List<Parcel> getAllParcels() {
        return repository.getAll();
    }
}
