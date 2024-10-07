package com.liga.appparcelsloading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckDto {
    @NotBlank
    private String nameTruck;
    @NotBlank
    private String nameParcels;
    @NotBlank
    private char[][] parcels;
}
