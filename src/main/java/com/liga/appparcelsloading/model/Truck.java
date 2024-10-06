package com.liga.appparcelsloading.model;

import com.liga.appparcelsloading.converter.CharArrayToJsonConverter;
import com.liga.appparcelsloading.converter.StringListToArrayConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String nameTruck;
    @Convert(converter = StringListToArrayConverter.class)
    @Column(columnDefinition = "text[]")
    private List<String> nameParcels;
    @Convert(converter = CharArrayToJsonConverter.class)
    @Column(columnDefinition = "json")
    private char[][] parcels;
}
