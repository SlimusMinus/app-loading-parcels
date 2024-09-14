package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.exception.NotFoundException;
import com.liga.loadingParcelsApp.model.Package;

import java.util.Arrays;
import java.util.List;

public class ValidationParcels {
    private static final int[][] nine = new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};
    private static final int[][] eight = new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}};
    private static final int[][] seven = new int[][]{{7, 7, 7}, {7, 7, 7, 7}};
    private static final int[][] six = new int[][]{{6, 6, 6}, {6, 6, 6}};
    private static final int[][] five = new int[][]{{5, 5, 5, 5, 5}};
    private static final int[][] four = new int[][]{{4, 4, 4, 4}};
    private static final int[][] three = new int[][]{{3, 3, 3}};
    private static final int[][] two = new int[][]{{2, 2}};
    private static final int[][] one = new int[][]{{1}};

    public static boolean isValidation(List<Package> parcels) {
        boolean isValid;
        for (Package parcel : parcels) {
            int[][] content = parcel.getContent();
            isValid = switch (content[0][0]) {
                case 9 -> Arrays.deepEquals(content, nine);
                case 8 -> Arrays.deepEquals(content, eight);
                case 7 -> Arrays.deepEquals(content, seven);
                case 6 -> Arrays.deepEquals(content, six);
                case 5 -> Arrays.deepEquals(content, five);
                case 4 -> Arrays.deepEquals(content, four);
                case 3 -> Arrays.deepEquals(content, three);
                case 2 -> Arrays.deepEquals(content, two);
                case 1 -> Arrays.deepEquals(content, one);
                default -> throw new NotFoundException("Некорректное число: " + content[0][0]);
            };
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

}
