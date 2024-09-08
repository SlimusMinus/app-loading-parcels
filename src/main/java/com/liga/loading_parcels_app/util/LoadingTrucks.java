package com.liga.loading_parcels_app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadingTrucks {

    private static final int TRUCK_SIZE = 6;

    static char[][] createEmptyTruck() {
        char[][] emptyTruck = new char[TRUCK_SIZE][TRUCK_SIZE];
        for (int i = 0; i < TRUCK_SIZE; i++) {
            Arrays.fill(emptyTruck[i], ' ');
        }
        return emptyTruck;
    }

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

    static boolean placePackage(char[][] truck, int[][] parcel) {
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

    static boolean canPlace(char[][] truck, int[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            for (int j = 0; j < pack[i].length; j++) {
                if (pack[i][j] != 0 && truck[row + i][col + j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    static void applyPackage(char[][] truck, int[][] pack, int row, int col) {
        for (int i = 0; i < pack.length; i++) {
            for (int j = 0; j < pack[i].length; j++) {
                if (pack[i][j] != 0) {
                    truck[row + i][col + j] = (char) ('0' + pack[i][j]);
                }
            }
        }
    }

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
