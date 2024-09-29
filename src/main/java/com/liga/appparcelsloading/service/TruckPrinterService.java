package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
public class TruckPrinterService {
    private ParcelMapper parcelMapper;
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
