package com.liga.appparcelsloading.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullTruck {
    private String nameTruck;
    private List<String> nameParcels;
    private char[][] parcels;
}
