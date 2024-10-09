package com.liga.appparcelsloading.truck.service;

import com.liga.appparcelsloading.truck.model.Dimension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для создания грузовиков.
 * Предоставляет методы для создания пустых грузовиков с заданными размерами.
 */
@Slf4j
@Service
public class TruckFactoryService {

    /**
     * Создает список пустых грузовиков на основе переданных размеров.
     *
     * @param dimensionsTrucks список объектов Dimension, представляющих размеры грузовиков
     * @return список пустых грузовиков в виде двумерных массивов символов,
     *         где ' ' обозначает пустое пространство
     */
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
