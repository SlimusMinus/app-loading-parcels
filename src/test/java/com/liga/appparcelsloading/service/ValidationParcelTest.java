package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.util.validator.ParcelValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.liga.appparcelsloading.DataTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование класса ValidationData")
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class ValidationParcelTest {
    @Autowired
    private ParcelValidator parcelValidator;

    @Test
    @DisplayName("тестирование валидных данных")
    void isValidationTrue() {
        assertThat(parcelValidator.isValid(PARCELS)).isTrue();
    }

    @Test
    @DisplayName("тестирование не валидных данных")
    void isValidationFalse() {
        assertThat(parcelValidator.isValid(PARCEL_WRONG)).isFalse();
    }

}