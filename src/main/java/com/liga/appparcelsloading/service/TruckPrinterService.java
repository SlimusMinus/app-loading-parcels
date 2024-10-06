package com.liga.appparcelsloading.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для печати содержимого грузовиков.
 * Предоставляет методы для отображения информации о грузовиках на экране.
 */
@Slf4j
@Service
public class TruckPrinterService {
    /**
     * Выводит на экран содержимое всех грузовиков.
     *
     * @param trucks Список грузовиков, каждый из которых представлен двумерным массивом символов.
     */
    public void printTrucks(List<char[][]> trucks) {
        log.info("Начало вывода содержимого грузовика(ов).");
        System.out.println("++++++++");
        for (char[][] truck : trucks) {
            for (char[] row : truck) {
                System.out.print("+");
                for (char cell : row) {
                    System.out.print(cell);
                }
                System.out.print("+\n");
            }
            System.out.println("++++++++");
        }
    }
}
