package com.liga.appparcelsloading.validator;

import java.util.List;

public class TruckCountValidate {
    /**
     * Проверяет, что количество грузовиков достаточно для загрузки всех посылок.
     *
     * @param countTruck запланированное количество грузовиков
     * @param trucks     фактическое количество загруженных грузовиков
     */
    public boolean validateTruckCount(int countTruck, List<char[][]> trucks) {
        return countTruck < trucks.size();
    }
}
