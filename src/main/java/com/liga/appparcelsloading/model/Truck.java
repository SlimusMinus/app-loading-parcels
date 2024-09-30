package com.liga.appparcelsloading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс представляет грузовик с определённым названием и списком посылок.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Truck {
    private String name;
    private int height;
    private int weight;
    private List<Integer> parcels;
}
