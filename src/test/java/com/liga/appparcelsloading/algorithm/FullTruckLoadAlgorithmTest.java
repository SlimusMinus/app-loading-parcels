package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.util.ParcelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.liga.appparcelsloading.DataTest.PARCEL;
import static com.liga.appparcelsloading.DataTest.SYMBOLS;
import static org.assertj.core.api.Assertions.assertThat;

class FullTruckLoadAlgorithmTest {

    private ParcelMapper parcelMapper;

    @BeforeEach
    void setUp() {
        parcelMapper = new ParcelMapper();
    }

    @Test
    void getSymbolParcels() {
        char[][] symbolParcels = parcelMapper.getSymbolParcels(PARCEL, new int[][]{{3, 3, 3}});
        assertThat(symbolParcels).isEqualTo(SYMBOLS);
    }
}