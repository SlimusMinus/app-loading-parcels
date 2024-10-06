package com.liga.appparcelsloading.model;

import com.liga.appparcelsloading.converter.IntArrayToJsonConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Представляет упаковку с содержимым в виде двумерного массива.
 * Каждый элемент массива содержит информацию о содержимом упаковки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parcels")
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int parcelId;
    private String name;
    private char symbol;
    @Convert(converter = IntArrayToJsonConverter.class)
    @Column(columnDefinition = "jsonb")
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
