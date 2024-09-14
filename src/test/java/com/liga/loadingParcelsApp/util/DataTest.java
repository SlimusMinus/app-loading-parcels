package com.liga.loadingParcelsApp.util;

import com.liga.loadingParcelsApp.model.Package;

import java.util.Arrays;
import java.util.List;

public class DataTest {
    public static final int TRUCK_SIZE = 6;
    private static final int[][] nine = new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};
    private static final int[][] eight = new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}};
    private static final int[][] seven = new int[][]{{7, 7, 7}, {7, 7, 7, 7}};
    private static final int[][] six = new int[][]{{6, 6, 6}, {6, 6, 6}};
    private static final int[][] five = new int[][]{{5, 5, 5, 5, 5}};
    private static final int[][] four = new int[][]{{4, 4, 4, 4}};
    private static final int[][] three = new int[][]{{3, 3, 3}};
    private static final int[][] two = new int[][]{{2, 2}};
    private static final int[][] one = new int[][]{{1}};
    private static final int[][] wrongOne = new int[][]{{568}};
    private static final int[][] wrongTwo = new int[][]{{568}, {896}};
    private static final int[][] nineWrong = new int[][]{{9, 7, 9}, {9, 8, 9}, {9, 9, 9}};
    private static final int[][] eightWrong = new int[][]{{8, 4, 8, 8}, {8, 4, 8, 8}};
    public static final List<Package> parcels = Arrays.asList(
            new Package(nine),
            new Package(eight),
            new Package(seven),
            new Package(five),
            new Package(four),
            new Package(three),
            new Package(two),
            new Package(one)
    );

    public static final List<Package> parcelsWrong = Arrays.asList(
            new Package(wrongOne),
            new Package(wrongTwo)
    );

    public static final List<Package> parcelsWrong2 = Arrays.asList(
            new Package(nineWrong),
            new Package(eightWrong)
    );
}
