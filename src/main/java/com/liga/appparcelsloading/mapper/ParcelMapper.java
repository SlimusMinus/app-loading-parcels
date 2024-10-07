package com.liga.appparcelsloading.mapper;

import com.liga.appparcelsloading.dto.ParcelDto;
import com.liga.appparcelsloading.model.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParcelMapper {
    ParcelMapper INSTANCE = Mappers.getMapper( ParcelMapper.class);
    Parcel getParcel(Parcel parcel);
    ParcelDto getParcelDto( Parcel parcelDto);
}
