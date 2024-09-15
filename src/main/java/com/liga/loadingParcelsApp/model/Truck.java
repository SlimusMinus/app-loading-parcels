package com.liga.loadingParcelsApp.model;

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
    /**
     * Название грузовика.
     */
    private String name;

    /**
     * Список посылок, которые находятся в грузовике.
     * Каждый элемент списка представляет размер одной посылки.
     */
    private List<Integer> parcels;
}
