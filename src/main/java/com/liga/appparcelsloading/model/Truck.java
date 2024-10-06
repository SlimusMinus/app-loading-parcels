package com.liga.appparcelsloading.model;

import com.liga.appparcelsloading.converter.CharArrayToJsonConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trucks")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_truck")
    private String nameTruck;
    @Column(name = "name_parcels")
    private String nameParcels;
    @Convert(converter = CharArrayToJsonConverter.class)
    @Column(columnDefinition = "json")
    private char[][] parcels;

    public Truck(String nameTruck, String nameParcels, char[][] parcels) {
        this.nameTruck = nameTruck;
        this.nameParcels = nameParcels;
        this.parcels = parcels;
    }
}
