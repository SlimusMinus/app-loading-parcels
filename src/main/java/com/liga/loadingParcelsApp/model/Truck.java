package com.liga.loadingParcelsApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Truck {
    private String name;
    private List<Integer> parcels;
}
