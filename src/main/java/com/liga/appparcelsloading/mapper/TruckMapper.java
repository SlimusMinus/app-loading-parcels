package com.liga.appparcelsloading.mapper;

import com.liga.appparcelsloading.dto.TruckDto;
import com.liga.appparcelsloading.model.Truck;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TruckMapper {
    TruckMapper INSTANCE = Mappers.getMapper(TruckMapper.class);
    Truck getTruck(TruckDto truck);
    TruckDto getTruckDto(Truck truckDto);
}
