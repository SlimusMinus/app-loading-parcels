package com.liga.appparcelsloading.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
public class TruckFactoryService {
    /**
     * Создает пустой грузовик размером 6x6, заполненный пробелами ' '.
     *
     * @return двумерный массив символов, представляющий пустой грузовик.
     */
    public char[][] createEmptyTruck(int truckSize) {
        log.trace("Создание пустого грузовика.");

        char[][] emptyTruck = new char[truckSize][truckSize];
        for (int i = 0; i < truckSize; i++) {
            Arrays.fill(emptyTruck[i], ' ');
        }
        return emptyTruck;
    }
}
