package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EvenTruckLoadingRestAlgorithm implements TruckRestAlgorithm{
    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, List<Dimension> dimensionsTrucks) {
        return List.of();
    }

    @Override
    public List<Truck> loadParcelsByName(String nameParcels, List<Dimension> dimensionsTrucks) {
        return List.of();
    }
}
