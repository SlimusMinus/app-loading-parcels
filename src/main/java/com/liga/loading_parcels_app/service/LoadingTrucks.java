package com.liga.loading_parcels_app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadingTrucks {

    private static final int TRUCK_SIZE = 6;

    //Этот метод создает пустой грузовик размером 6x6, заполненный пробелами ' '
    public static char[][] createEmptyTruck() {
        char[][] emptyTruck = new char[TRUCK_SIZE][TRUCK_SIZE];
        for (int i = 0; i < TRUCK_SIZE; i++) {
            Arrays.fill(emptyTruck[i], ' ');
        }
        return emptyTruck;
    }

    //Этот метод принимает список посылок и пакует их в грузовики
    public static List<char[][]> packPackages(List<int[][]> parcels) {
        List<char[][]> trucks = new ArrayList<>();
        char[][] emptyTruck = createEmptyTruck();

        for (int[][] parcel : parcels) {
            if (!placePackage(emptyTruck, parcel)) {
                trucks.add(emptyTruck);
                emptyTruck = createEmptyTruck();
                placePackage(emptyTruck, parcel);
            }
        }
        trucks.add(emptyTruck);
        return trucks;
    }

    //Этот метод пытается разместить одну посылку в грузовике
    public static boolean placePackage(char[][] truck, int[][] parcel) {
        for (int i = TRUCK_SIZE - parcel.length; i >= 0; i--) {
            for (int j = 0; j <= TRUCK_SIZE - parcel[0].length; j++) {
                if (canPlace(truck, parcel, i, j)) {
                    applyPackage(truck, parcel, i, j);
                    return true;
                }
            }
        }
        return false;
    }

    //Этот метод проверяет, можно ли поставить посылку в конкретное место (на координаты row, col) грузовика
    public static boolean canPlace(char[][] truck, int[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            for (int j = 0; j < pack[i].length; j++) {
                if (truck[row + i][col + j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    //Этот метод действительно размещает посылку в грузовике
    public static void applyPackage(char[][] truck, int[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            for (int j = 0; j < pack[i].length; j++) {
                truck[row + i][col + j] = (char) ('0' + pack[i][j]);
            }
        }
    }

    //Этот метод выводит на экран все грузовики с их содержимым
    public static void printTrucks(List<char[][]> trucks) {
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
