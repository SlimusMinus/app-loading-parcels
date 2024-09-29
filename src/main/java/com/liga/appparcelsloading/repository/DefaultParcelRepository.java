package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.util.ParcelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultParcelRepository implements ParcelRepository {
    private final Map<String, Parcel> parcels;

    public DefaultParcelRepository(ParcelMapper parcelMapper) {
        this.parcels = parcelMapper.getAllParcels();
    }

    @Override
    public Parcel save(Parcel parcel) {
        return parcels.put(parcel.getName(), parcel);
    }

    @Override
    public Parcel update(Parcel parcel) {
        return parcels.put(parcel.getName(), parcel);
    }

    @Override
    public Parcel getByName(String name) {
        return parcels.get(name);
    }

    @Override
    public List<Parcel> getAll() {
        return new ArrayList<>(parcels.values());
    }

    @Override
    public boolean delete(String name) {
        return parcels.remove(name) != null;
    }
}
