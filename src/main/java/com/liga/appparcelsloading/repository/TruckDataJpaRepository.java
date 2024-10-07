package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TruckDataJpaRepository extends JpaRepository<Truck, Integer> {
    @Modifying
    @Query("DELETE FROM Truck t WHERE t.id = :id")
    int deleteById(int id);
}
