package com.liga.appparcelsloading.algorithm;

import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;

import java.util.List;

public interface TruckLoadAlgorithm {
    List<Truck> loadParcels(List<Parcel> parcels, List<Dimension> dimensionsTrucks);

    List<Truck> loadParcelsByName(String nameParcels, List<Dimension> dimensionsTrucks);
}
