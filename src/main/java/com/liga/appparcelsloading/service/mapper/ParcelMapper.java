package com.liga.appparcelsloading.service.mapper;

import com.liga.appparcelsloading.service.dto.ParcelDto;
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
 *     <li>{@link #toParcelDto(Parcel)} — преобразует сущность {@link Parcel} в DTO {@link ParcelDto}.</li>
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
     * @param parcel объект посылки
     * @return DTO посылки
     */
    ParcelDto toParcelDto(Parcel parcel);

    /**
     * Преобразует объект DTO {@link ParcelDto} в сущность {@link Parcel}.
     *
     * @param parcelDto DTO посылки
     * @return объект посылки
     */
    Parcel toParcel(ParcelDto parcelDto);
}
