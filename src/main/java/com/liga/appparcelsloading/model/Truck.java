package com.liga.appparcelsloading.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Класс представляет грузовик с определённым названием и списком посылок.
 */
@Data
@AllArgsConstructor
public class Truck {
    private final String name;
    private final List<Integer> parcels;
}
