package com.liga.loadingParcelsApp.util;

import com.liga.loadingParcelsApp.service.ValidationParcels;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.liga.loadingParcelsApp.util.DataTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование класса ValidationData")
class ValidationParcelsTest {
    @Test
    @DisplayName("тестирование валидных данных")
    void isValidationTrue() {
        assertThat(ValidationParcels.isValidation(parcels)).isTrue();
    }

    @Test
    @DisplayName("тестирование не валидных данных")
    void isValidationFalse() {
        assertThat(ValidationParcels.isValidation(parcelsWrong2)).isFalse();
    }

    @Test
    @DisplayName("выброс исключение при некорректных данных")
    void isValidationIncorrectNumber() {
        assertThatThrownBy(() -> ValidationParcels.isValidation(parcelsWrong)).isInstanceOf(IllegalArgumentException.class);
    }

}