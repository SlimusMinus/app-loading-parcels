package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.TruckInMemoryService;
import com.liga.appparcelsloading.util.FileReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Optional;


@Slf4j
@AllArgsConstructor
@ShellComponent
public class TruckController {
    private final TruckInMemoryService truckInMemoryService;

    @ShellMethod(value = "Погрузка посылок (even-равномерная погрузка, optimal-оптимальная погрузка)", key = "load")
    public Optional<List<char[][]>> load(String algorithmType, String heights, String weights) {
        //load even 6,6,7 8,8,8
        //load optimal 6,6 8,8
        return truckInMemoryService.load(algorithmType, heights, weights);
    }

    @ShellMethod(value = "Погрузка посылок по именам (even-равномерная погрузка, optimal-оптимальная погрузка)", key = "load-by-name")
    public Optional<List<char[][]>> loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        //load-by-name optimal Кофемашина,Холодильник,Пылесос,Наушники 6,6 8,8
        //load-by-name even Кофемашина,Холодильник,Пылесос,Наушники,Телевизор 6,6,6 7,7,8
       return truckInMemoryService.loadByName(algorithmType, nameParcels, heights, weights);
    }

    @ShellMethod(value = "Показать содержимое грузовиков", key = "show-fullTrucks")
    public List<Truck> showTrucks() {
        return truckInMemoryService.showTrucks();
    }
}
