package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckDataJpaRepository extends JpaRepository<Truck, Integer> {
}
