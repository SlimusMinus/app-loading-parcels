package com.liga.appparcelsloading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDto {
    @NotBlank
    private String name;
    @NotBlank
    private char symbol;
    @NotBlank
    private int[][] form;
}
