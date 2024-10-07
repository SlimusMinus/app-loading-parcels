package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.util.ParcelDataMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.liga.appparcelsloading.DataTest.PARCEL;
import static com.liga.appparcelsloading.DataTest.SYMBOLS;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class TruckLoadAlgorithmTest {

    @Autowired
    private ParcelDataMapper parcelDataMapper;


    @Test
    void getSymbolParcels() {
        char[][] symbolParcels = parcelDataMapper.getSymbolParcels(PARCEL, new int[][]{{3, 3, 3}});
        assertThat(symbolParcels).isEqualTo(SYMBOLS);
    }
}