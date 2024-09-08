package com.liga.loading_parcels_app.util;

import java.util.Arrays;
import java.util.List;

public class ValidationData {
    private static final int[][] nine = new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};
    private static final int[][] eight = new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}};
    private static final int[][] seven = new int[][]{{7, 7, 7}, {7, 7, 7, 7}};
    private static final int[][] six = new int[][]{{6, 6, 6}, {6, 6, 6}};
    private static final int[][] five = new int[][]{{5, 5, 5, 5, 5}};
    private static final int[][] four = new int[][]{{4, 4, 4, 4}};
    private static final int[][] three = new int[][]{{3, 3, 3}};
    private static final int[][] two = new int[][]{{2, 2}};
    private static final int[][] one = new int[][]{{1}};

    public static boolean isValidation(List<int[][]> parcels) {
        boolean isValid;
        for (int[][] parcel : parcels) {
            isValid = switch (parcel[0][0]) {
                case 9 -> Arrays.deepEquals(parcel, nine);
                case 8 -> Arrays.deepEquals(parcel, eight);
                case 7 -> Arrays.deepEquals(parcel, seven);
                case 6 -> Arrays.deepEquals(parcel, six);
                case 5 -> Arrays.deepEquals(parcel, five);
                case 4 -> Arrays.deepEquals(parcel, four);
                case 3 -> Arrays.deepEquals(parcel, three);
                case 2 -> Arrays.deepEquals(parcel, two);
                case 1 -> Arrays.deepEquals(parcel, one);
                default -> throw new NotFoundException("Incorrect number");
            };
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

}
