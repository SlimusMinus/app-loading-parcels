package com.liga.appparcelsloading.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.liga.appparcelsloading.DataTest.PARCELS_FROM_FILE;
import static org.assertj.core.api.Assertions.assertThat;

class ParcelMapperTest {
    private final ParcelMapper parcelMapper = new ParcelMapper();

    @Test
    @DisplayName("Проверка количества посылок прочитанных из файла")
    void testParcelMapper() {
        assertThat(parcelMapper.getAllParcels().size()).isEqualTo(PARCELS_FROM_FILE);
    }

}