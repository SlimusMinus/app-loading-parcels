package com.liga.loadingParcelsApp.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TruckPrinter {
    /**
     * Выводит на экран содержимое всех грузовиков.
     *
     * @param trucks Список грузовиков, каждый из которых представлен двумерным массивом символов.
     */
    public void printTrucks(List<char[][]> trucks) {
        log.info("Начало вывода содержимого {} грузовиков.", trucks.size());
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
        log.info("Вывод содержимого завершен.");
    }
}
