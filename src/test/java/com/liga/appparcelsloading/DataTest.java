package com.liga.appparcelsloading;

import com.liga.appparcelsloading.model.Parcel;

import java.util.*;

public class DataTest {
    public static final int TRUCK_SIZE = 6;
    public static final int PARCELS_FROM_FILE = 10;
    public static final Parcel PARCEL = new Parcel("Стиральная машина", '+', new int[][]{{3, 3, 3}});
    public static final char[][] SYMBOLS =  new char[][]{{'+', '+', '+'}};
    public static final Parcel GET_BY_NAME_PARCEL = new Parcel("Чайник", '%', new int[][]{{5, 5, 5, 5, 5}});

    private static final int[][] nine = new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};
    private static final int[][] eight = new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}};
    private static final int[][] seven = new int[][]{{7, 7, 7}, {7, 7, 7, 7}};
    private static final int[][] five = new int[][]{{5, 5, 5, 5, 5}};
    private static final int[][] four = new int[][]{{4, 4, 4, 4}};
    private static final int[][] three = new int[][]{{3, 3, 3}};
    private static final int[][] two = new int[][]{{2, 2}};
    private static final int[][] one = new int[][]{{1}};
    private static final int[][] wrongOne = new int[][]{{568}};
    private static final int[][] wrongTwo = new int[][]{{568}, {896}};
    private static final int[][] nineWrong = new int[][]{{9, 7, 9}, {9, 8, 9}, {9, 9, 9}};
    private static final int[][] eightWrong = new int[][]{{8, 4, 8, 8}, {8, 4, 8, 8}};
    public static final Map<Integer, List<Integer>> loadingTrucks = new HashMap<>();
    static {
        List<Integer> truck1Parcels = new ArrayList<>();
        truck1Parcels.add(10);
        truck1Parcels.add(20);
        loadingTrucks.put(1, truck1Parcels);

        List<Integer> truck2Parcels = new ArrayList<>();
        truck2Parcels.add(15);
        loadingTrucks.put(2, truck2Parcels);
    }

    public static final List<Parcel> PARCELS = Arrays.asList(
            new Parcel(nine),
            new Parcel(eight),
            new Parcel(seven),
            new Parcel(five),
            new Parcel(four),
            new Parcel(three),
            new Parcel(two),
            new Parcel(one)
    );

    public static final List<Parcel> PARCEL_WRONG = Arrays.asList(
            new Parcel(wrongOne),
            new Parcel(wrongTwo)
    );

    public static final List<Parcel> PARCEL_WRONG_2 = Arrays.asList(
            new Parcel(nineWrong),
            new Parcel(eightWrong)
    );
}
