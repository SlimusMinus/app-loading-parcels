package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.model.Dimension;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TruckFactoryService {

    public List<char[][]> createEmptyTruck(List<Dimension> dimensionsTrucks) {
        log.trace("Создание пустого грузовика.");
        List<char[][]> emptyTrucks = new ArrayList<>();
        for (Dimension dimension : dimensionsTrucks) {
            char[][] emptyTruck = new char[dimension.getHeight()][dimension.getWidth()];
            for (int i = 0; i < dimension.getHeight(); i++) {
                for (int j = 0; j < dimension.getWidth(); j++) {
                    emptyTruck[i][j] = ' ';
                }
            }
            emptyTrucks.add(emptyTruck);
        }
        return emptyTrucks;
    }
}
