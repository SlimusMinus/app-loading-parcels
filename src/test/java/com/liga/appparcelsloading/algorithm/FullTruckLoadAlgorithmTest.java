package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.util.ParcelMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.liga.appparcelsloading.DataTest.PARCEL;
import static com.liga.appparcelsloading.DataTest.SYMBOLS;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class FullTruckLoadAlgorithmTest {

    @Autowired
    private ParcelMapper parcelMapper;


    @Test
    void getSymbolParcels() {
        char[][] symbolParcels = parcelMapper.getSymbolParcels(PARCEL, new int[][]{{3, 3, 3}});
        assertThat(symbolParcels).isEqualTo(SYMBOLS);
    }
}