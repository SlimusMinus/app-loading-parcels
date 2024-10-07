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

/**
 * Контроллер для работы с погрузкой посылок в грузовики с использованием команд Spring Shell.
 *
 * Предоставляет команды для выбора алгоритма погрузки (равномерная или оптимальная),
 * а также для загрузки посылок по именам или по параметрам и просмотра содержимого грузовиков.
 *
 * Взаимодействует с сервисом {@link TruckInMemoryService}, который обрабатывает данные о грузовиках и посылках.
 */
@Slf4j
@AllArgsConstructor
@ShellComponent
public class TruckController {
    private final TruckInMemoryService truckInMemoryService;

    /**
     * Выполняет погрузку посылок в грузовики с использованием указанного алгоритма.
     * Доступные алгоритмы: "even" (равномерная погрузка), "optimal" (оптимальная погрузка).
     *
     * Пример команды: `load even 6,6,7 8,8,8`
     *
     * @param algorithmType Тип алгоритма погрузки ("even" или "optimal").
     * @param heights       Список высот грузовиков, разделенных запятыми.
     * @param weights       Список грузоподъемностей грузовиков, разделенных запятыми.
     * @return Список массивов символов, представляющих загруженные грузовики.
     */
    @ShellMethod(value = "Погрузка посылок (even-равномерная погрузка, optimal-оптимальная погрузка)", key = "load")
    public Optional<List<char[][]>> load(String algorithmType, String heights, String weights) {
        //load even 6,6,7 8,8,8
        //load optimal 6,6 8,8
        return truckInMemoryService.load(algorithmType, heights, weights);
    }

    /**
     * Выполняет погрузку посылок в грузовики по их именам с использованием указанного алгоритма.
     * Доступные алгоритмы: "even" (равномерная погрузка), "optimal" (оптимальная погрузка).
     *
     * Пример команды: `load-by-name optimal Кофемашина,Холодильник,Пылесос 6,6 8,8`
     *
     * @param algorithmType Тип алгоритма погрузки ("even" или "optimal").
     * @param nameParcels   Имена посылок, разделенные запятыми.
     * @param heights       Список высот грузовиков, разделенных запятыми.
     * @param weights       Список грузоподъемностей грузовиков, разделенных запятыми.
     * @return Список массивов символов, представляющих загруженные грузовики.
     */
    @ShellMethod(value = "Погрузка посылок по именам (even-равномерная погрузка, optimal-оптимальная погрузка)", key = "load-by-name")
    public Optional<List<char[][]>> loadByName(String algorithmType, String nameParcels, String heights, String weights) {
        //load-by-name optimal Кофемашина,Холодильник,Пылесос,Наушники 6,6 8,8
        //load-by-name even Кофемашина,Холодильник,Пылесос,Наушники,Телевизор 6,6,6 7,7,8
       return truckInMemoryService.loadByName(algorithmType, nameParcels, heights, weights);
    }

    /**
     * Показывает содержимое всех загруженных грузовиков.
     *
     * @return Список объектов {@link Truck}, представляющих загруженные грузовики.
     */
    @ShellMethod(value = "Показать содержимое грузовиков", key = "show-fullTrucks")
    public List<Truck> showTrucks() {
        return truckInMemoryService.showTrucks();
    }
}
