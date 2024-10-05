package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.service.TruckService;
import com.liga.appparcelsloading.util.FileReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

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
public class TruckController {
    private final TruckService truckService;

    @ShellMethod(value = "Погрузка посылок (even-равномерная погрузка, optimal-оптимальная погрузка)", key = "load")
    public void loadEvenly(String algorithmType, String heights, String weights) {
        truckService.load(algorithmType, heights, weights);
    }

    @ShellMethod(value = "Погрузка посылок по именам (even-равномерная погрузка, optimal-оптимальная погрузка)", key = "load-by-name")
    public void loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        truckService.loadByName(algorithmType, nameParcels, heights, weights);
    }

    @ShellMethod(value = "Показать содержимое грузовиков", key = "show-fullTrucks")
    public void showTrucks() {
        truckService.showTrucks();
    }
}
