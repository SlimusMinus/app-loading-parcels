package com.liga.appparcelsloading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) для представления данных о грузовике и посылках, загруженных в него.
 *
 * Этот класс используется для передачи информации о грузовике и его содержимом между слоями приложения
 * (например, между контроллерами и сервисами).
 *
 * Поля:
 * <ul>
 *     <li>{@code nameTruck} — имя грузовика (не может быть пустым)</li>
 *     <li>{@code nameParcels} — строковое представление имен посылок (не может быть пустым)</li>
 *     <li>{@code parcels} — двумерный массив символов, представляющий загруженные посылки (не может быть пустым)</li>
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
public class TruckDto {
    @NotBlank
    private String nameTruck;
    @NotBlank
    private String nameParcels;
    @NotBlank
    private char[][] parcels;
}
