package com.liga.loadingParcelsApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Представляет упаковку с содержимым в виде двумерного массива.
 * Каждый элемент массива содержит информацию о содержимом упаковки.
 */
@Getter
@AllArgsConstructor
public class Parcel {
    private final int[][] content;
}
