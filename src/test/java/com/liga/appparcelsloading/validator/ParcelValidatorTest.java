package com.liga.appparcelsloading.validator;

import com.liga.appparcelsloading.parcel.model.Parcel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class ParcelValidatorTest {

    @Autowired
    private ParcelValidator parcelValidator;

    @Test
    @DisplayName("Проверка валидной посылки")
    void testValidParcel() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(createParcel(new int[][]{{1}, {2, 2}}));
        parcels.add(createParcel(new int[][]{{3, 3, 3}, {4, 4, 4, 4}}));

        boolean isValid = parcelValidator.isValid(parcels);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Проверка невалидной посылки")
    void testInvalidParcel() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(createParcel(new int[][]{{1, 1}, {2, 2}}));
        parcels.add(createParcel(new int[][]{{3, 3, 3}, {4, 4}}));

        boolean isValid = parcelValidator.isValid(parcels);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Проверка пустого списка посылок")
    void testEmptyParcels() {
        List<Parcel> parcels = new ArrayList<>();

        boolean isValid = parcelValidator.isValid(parcels);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Проверка посылки с нулевыми значениями")
    void testParcelWithZeros() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(createParcel(new int[][]{{0, 0}, {0, 0}}));

        boolean isValid = parcelValidator.isValid(parcels);

        assertThat(isValid).isFalse();
    }

    private Parcel createParcel(int[][] content) {
        Parcel parcel = new Parcel();
        parcel.setForm(content);
        return parcel;
    }
}