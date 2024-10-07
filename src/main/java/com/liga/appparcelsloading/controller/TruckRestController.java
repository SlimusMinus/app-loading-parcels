package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.dto.TruckDto;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.TruckRestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST-контроллер для управления грузовиками и погрузкой посылок.
 *
 * Предоставляет API для выполнения погрузки посылок в грузовики с использованием различных алгоритмов,
 * а также для получения информации о грузовиках и удаления грузовиков по ID.
 *
 * Взаимодействует с сервисом {@link TruckRestService}, который реализует основную логику работы с грузовиками.
 */
@RestController
@RequestMapping("/trucks")
@AllArgsConstructor
public class TruckRestController {
    private final TruckRestService truckRestService;

    /**
     * Выполняет погрузку посылок в грузовики с использованием указанного алгоритма.
     * Доступные алгоритмы: "even" (равномерная погрузка), "optimal" (оптимальная погрузка).
     *
     * @param algorithmType Тип алгоритма погрузки ("even" или "optimal").
     * @param heights       Список высот грузовиков в строковом формате, разделенный запятыми.
     * @param weights       Список грузоподъемностей грузовиков в строковом формате, разделенный запятыми.
     * @return Ответ с загруженными грузовиками или ошибкой, если данные некорректны.
     */
    @PostMapping("/load")
    public ResponseEntity<List<Truck>> loadTrucks(
            @RequestParam String algorithmType,
            @RequestParam String heights,
            @RequestParam String weights) {
        Optional<List<Truck>> loadedTrucks = truckRestService.load(algorithmType, heights, weights);
        return loadedTrucks
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    /**
     * Выполняет погрузку посылок по именам в грузовики с использованием указанного алгоритма.
     * Доступные алгоритмы: "even" (равномерная погрузка), "optimal" (оптимальная погрузка).
     *
     * @param algorithmType Тип алгоритма погрузки ("even" или "optimal").
     * @param nameParcels   Имена посылок, разделенные запятыми.
     * @param heights       Список высот грузовиков в строковом формате, разделенный запятыми.
     * @param weights       Список грузоподъемностей грузовиков в строковом формате, разделенный запятыми.
     * @return Ответ с загруженными грузовиками или ошибкой, если данные некорректны.
     */
    @PostMapping("/load-by-name")
    public ResponseEntity<List<Truck>> loadTrucksByName(
            @RequestParam String algorithmType,
            @RequestParam String nameParcels,
            @RequestParam String heights,
            @RequestParam String weights) {
        Optional<List<Truck>> loadedTrucks = truckRestService.loadByName(algorithmType, nameParcels, heights, weights);
        return loadedTrucks
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    /**
     * Возвращает список всех грузовиков.
     *
     * @return Список объектов {@link TruckDto}, представляющих все доступные грузовики.
     */
    @GetMapping
    public ResponseEntity<List<TruckDto>> getTrucks() {
        return truckRestService.findAll();
    }

    /**
     * Возвращает информацию о конкретном грузовике по его ID.
     *
     * @param id Идентификатор грузовика.
     * @return Ответ с объектом {@link TruckDto}, представляющим грузовик, или ошибкой, если грузовик не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TruckDto> getTruckById(@PathVariable int id){
        return truckRestService.findById(id);
    }

    /**
     * Удаляет грузовик по его ID.
     *
     * @param id Идентификатор грузовика.
     * @return Пустой ответ с кодом 204 (No Content), если удаление успешно, или ошибкой, если грузовик не найден.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruckById(@PathVariable int id) {
        return truckRestService.deleteById(id);
    }
}
