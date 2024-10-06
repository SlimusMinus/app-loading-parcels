package com.liga.appparcelsloading.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.liga.appparcelsloading.DataTest.PARCELS_FROM_FILE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class ParcelMapperTest {
    @Autowired
    private ParcelMapper parcelMapper;

    @Test
    @DisplayName("Проверка количества посылок прочитанных из файла")
    void testParcelMapper() {
        assertThat(parcelMapper.getAllParcels().size()).isEqualTo(PARCELS_FROM_FILE);
    }

}