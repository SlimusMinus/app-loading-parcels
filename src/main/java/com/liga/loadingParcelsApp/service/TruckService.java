package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Package;
import com.liga.loadingParcelsApp.util.FileReader;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

/**
 * Класс {@code TruckService} представляет собой сервис для управления процессом загрузки посылок в грузовики.
 * Он включает в себя метод для запуска процесса загрузки и взаимодействует с пользователем через консоль.
 * <p>
 * Класс использует {@link LoadingTrucks} для упаковки посылок и {@link FileReader} для чтения данных о посылках.
 * Также используется {@link ValidationParcels} для проверки валидности посылок и {@link JsonParser} для чтения данных из файла JSON.
 * </p>
 */
@Slf4j
public class TruckService {
    /**
     * Запускает процесс загрузки посылок и предоставляет пользователю выбор действий через консоль.
     * <p>
     * В этом методе выполняются следующие действия:
     * <ul>
     *     <li>Чтение списка посылок из файла "parcels.txt" с помощью {@link FileReader}.</li>
     *     <li>Проверка валидности посылок с использованием {@link ValidationParcels}.</li>
     *     <li>Предоставление пользователю меню для выбора алгоритма упаковки:</li>
     *     <ul>
     *         <li>1 - Равномерная загрузка по грузовикам (метод {@link LoadingTrucks#evenlyDistributePackages(List, int)}).</li>
     *         <li>2 - Оптимальная загрузка (метод {@link LoadingTrucks#packPackages(List, int)}).</li>
     *         <li>3 - Просмотр содержимого грузовиков из файла JSON с использованием {@link JsonParser#readJsonTrucks(String)}.</li>
     *     </ul>
     *     <li>В зависимости от выбранного действия, выводится информация о загрузке грузовиков в консоль и сохраняются данные в память с помощью {@link WriteTrucksInMemory}.</li>
     * </ul>
     * </p>
     */
    public static void startLoading() {
        log.info("Начало процесса загрузки посылок.");
        FileReader fileReader = new FileReader();
        List<Package> packages = fileReader.getAllPackages("parcels.txt");
        LoadingTrucks loadingTrucks = new LoadingTrucks();

        if (ValidationParcels.isValidation(packages)) {
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
                        List<char[][]> trucks = loadingTrucks.evenlyDistributePackages(packages, 4);
                        log.info("Успешно упаковано {} грузовиков.", trucks.size());
                        WriteTrucksInMemory.writeTrucks();
                        loadingTrucks.printTrucks(trucks);
                        break;
                    case 2: {
                        List<char[][]> trucks2 = loadingTrucks.packPackages(packages, 4);
                        log.info("Успешно упаковано {} грузовиков.", trucks2.size());
                        WriteTrucksInMemory.writeTrucks();
                        loadingTrucks.printTrucks(trucks2);
                        break;
                    }
                    case 3: {
                        JsonParser.readJsonTrucks("loading trucks.json");
                    }
                }
            } while (true);
        }
    }
}
