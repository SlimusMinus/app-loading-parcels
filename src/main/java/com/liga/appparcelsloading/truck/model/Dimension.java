package com.liga.appparcelsloading.truck.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет размеры объекта с заданной шириной и высотой.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dimension {
    private int width;
    private int height;
}
