package com.liga.appparcelsloading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) для представления данных о посылке.
 *
 * Этот класс используется для передачи данных о посылке между слоями приложения
 * (например, между контроллерами и сервисами), инкапсулируя основные атрибуты посылки.
 *
 * Поля:
 * <ul>
 *     <li>{@code name} — имя посылки (не может быть пустым)</li>
 *     <li>{@code symbol} — символ, представляющий посылку (не может быть пустым)</li>
 *     <li>{@code form} — форма посылки, представлена как двумерный массив целых чисел (не может быть пустым)</li>
 * </ul>
 *
 * Класс использует аннотации {@link Data} для автоматической генерации геттеров, сеттеров, toString, equals и hashCode,
 * {@link AllArgsConstructor} для генерации конструктора с параметрами для всех полей, и {@link NoArgsConstructor} для
 * генерации конструктора без параметров.
 *
 * Поля с аннотацией {@link NotBlank} указывают на то, что данные не должны быть пустыми или null.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDto {
    @NotBlank
    private String name;
    @NotBlank
    private char symbol;
    @NotBlank
    private int[][] form;
}
