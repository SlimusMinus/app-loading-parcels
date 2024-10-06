package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ParcelDataJpaRepository extends JpaRepository<Parcel, Integer> {
    @Modifying
    @Query("DELETE FROM Parcel p WHERE p.parcelId = :parcelId")
    int deleteByParcelId(int parcelId);
}
