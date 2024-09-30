package com.liga.appparcelsloading.validator;

import com.liga.appparcelsloading.service.ParcelLoaderService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Класс для валидации и управления загрузкой посылок в грузовики.
 */
@Slf4j
public class TruckCountValidate {
    /**
     * Проверяет, можно ли загрузить посылки в указанные грузовики.
     *
     * @param emptyTrucks         Список пустых грузовиков, представленных двумерными массивами символов.
     * @param counter             Количество грузовиков, необходимых для загрузки.
     * @param symbolParcels       Двумерный массив символов, представляющий посылки.
     * @param parcelLoaderService Сервис, отвечающий за загрузку посылок в грузовики.
     */
    public void validationFullTruck(List<char[][]> emptyTrucks, int counter, char[][] symbolParcels, ParcelLoaderService parcelLoaderService) {
        log.info("Начало проверки возможности загрузки посылок в грузовики. Необходимо грузовиков: {}", counter);
        char[][] truck;

        if (counter < emptyTrucks.size()) {
            truck = emptyTrucks.get(counter);
            parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length);
            log.info("Посылки успешно загружены в грузовик номер {}", counter);
        } else if (counter < emptyTrucks.size() + 1) {
            String errorMessage = "Не удалось загрузить посылки, необходимо " + counter + " грузовика(ов)";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        } else {
            String errorMessage = "Не хватает грузовиков для загрузки посылок. Требуется: " + counter + ", доступно: " + emptyTrucks.size();
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
