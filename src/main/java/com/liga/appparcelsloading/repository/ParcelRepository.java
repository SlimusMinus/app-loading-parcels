package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.model.Parcel;

import java.util.List;

public interface ParcelRepository {
    Parcel save(Parcel parcel);
    Parcel update(Parcel parcel);
    Parcel getByName(String name);
    List<Parcel> getAll();
    boolean delete(String name);
}
