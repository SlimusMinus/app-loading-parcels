package com.liga.appparcelsloading.mapper;

import com.liga.appparcelsloading.dto.ParcelDto;
import com.liga.appparcelsloading.model.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования данных между сущностью {@link Parcel} и DTO {@link ParcelDto}.
 *
 * Этот интерфейс использует библиотеку MapStruct для автоматической генерации реализации маппера.
 * Основная задача — преобразование данных между объектами Parcel и ParcelDto.
 *
 * Поля:
 * <ul>
 *     <li>{@code INSTANCE} — статическая переменная, содержащая единственный экземпляр маппера, создаваемый MapStruct.</li>
 * </ul>
 *
 * Методы:
 * <ul>
 *     <li>{@link #getParcelDto(Parcel)} — преобразует сущность {@link Parcel} в DTO {@link ParcelDto}.</li>
 * </ul>
 *
 * Использует:
 * <ul>
 *     <li>Аннотацию {@link Mapper} для указания MapStruct о необходимости создания реализации маппера.</li>
 * </ul>
 */
@Mapper
public interface ParcelMapper {
    ParcelMapper INSTANCE = Mappers.getMapper( ParcelMapper.class);

    /**
     * Преобразует объект {@link Parcel} в DTO {@link ParcelDto}.
     *
     */
    ParcelDto getParcelDto( Parcel parcelDto);
}
