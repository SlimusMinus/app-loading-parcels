package com.liga.loading_parcels_app.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.liga.loading_parcels_app.util.DataTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование класса ValidationData")
class ValidationDataTest {
    @Test
    @DisplayName("тестирование валидных данных")
    void isValidationTrue() {
        assertThat(ValidationData.isValidation(parcels)).isTrue();
    }

    @Test
    @DisplayName("тестирование не валидных данных")
    void isValidationFalse() {
        assertThat(ValidationData.isValidation(parcelsWrong2)).isFalse();
    }

    @Test
    @DisplayName("выброс исключение при некорректных данных")
    void isValidationIncorrectNumber() {
        assertThatThrownBy(() -> ValidationData.isValidation(parcelsWrong)).isInstanceOf(NotFoundException.class);
    }

}