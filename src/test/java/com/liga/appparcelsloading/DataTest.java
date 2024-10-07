package com.liga.appparcelsloading;

import com.liga.appparcelsloading.model.Parcel;

import java.util.*;

public class DataTest {
    public static final int TRUCK_SIZE = 6;
    public static final int NOT_VALID_ID = 999;
    public static final int UPDATE_ID = 1;
    public static final int DELETE_ID = 7;
    public static final int SIZE_AFTER_SAVE = 11;
    public static final int PARCELS_FROM_FILE = 10;
    public static final Parcel PARCEL = new Parcel("Стиральная машина", '+', new int[][]{{3, 3, 3}});
    public static final char[][] SYMBOLS = new char[][]{{'+', '+', '+'}};
    public static final Parcel GET_BY_NAME_PARCEL = new Parcel("Чайник", '%', new int[][]{{5, 5, 5, 5, 5}});

    public static final List<Parcel> PARCELS = Arrays.asList(
            new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
            new Parcel(new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}}),
            new Parcel(new int[][]{{7, 7, 7}, {7, 7, 7, 7}}),
            new Parcel(new int[][]{{5, 5, 5, 5, 5}}),
            new Parcel(new int[][]{{4, 4, 4, 4}}),
            new Parcel(new int[][]{{3, 3, 3}}),
            new Parcel( new int[][]{{2, 2}}),
            new Parcel(new int[][]{{1}})
    );

    public static final List<Parcel> PARCEL_WRONG = Arrays.asList(
            new Parcel(new int[][]{{9, 7, 9}, {9, 8, 9}, {9, 9, 9}}),
            new Parcel(new int[][]{{8, 4, 8, 8}, {8, 4, 8, 8}})
    );
}
