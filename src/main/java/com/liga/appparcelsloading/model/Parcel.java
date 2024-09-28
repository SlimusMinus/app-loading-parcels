package com.liga.appparcelsloading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет упаковку с содержимым в виде двумерного массива.
 * Каждый элемент массива содержит информацию о содержимом упаковки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parcel {
    private int[][] content;
}
