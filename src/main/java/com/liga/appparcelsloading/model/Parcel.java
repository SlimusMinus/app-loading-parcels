package com.liga.appparcelsloading.model;

import jakarta.persistence.Id;
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
    @Id
    private int parcelId;
    private String name;
    private char symbol;
    private int[][] form;

    public Parcel(int[][] form) {
        this.form = form;
    }

    public Parcel(String name, char symbol, int[][] form) {
        this.name = name;
        this.symbol = symbol;
        this.form = form;
    }

}
