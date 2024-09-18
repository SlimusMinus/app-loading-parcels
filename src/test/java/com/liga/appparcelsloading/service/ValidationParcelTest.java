package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.validator.ParcelValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.liga.appparcelsloading.DataTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование класса ValidationData")
class ValidationParcelTest {
    private ParcelValidator parcelValidator;

    @BeforeEach
    void setUp(){
        parcelValidator = new ParcelValidator();
    }
    @Test
    @DisplayName("тестирование валидных данных")
    void isValidationTrue() {
        assertThat(parcelValidator.isValid(PARCELS)).isTrue();
    }

    @Test
    @DisplayName("тестирование не валидных данных")
    void isValidationFalse() {
        assertThat(parcelValidator.isValid(PARCEL_WRONG_2)).isFalse();
    }

}