package com.liga.appparcelsloading.truck.mapper;

import com.liga.appparcelsloading.truck.dto.TruckDto;
import com.liga.appparcelsloading.truck.model.Truck;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования данных между сущностью {@link Truck} и DTO {@link TruckDto}.
 *
 * Этот интерфейс использует библиотеку MapStruct для автоматической генерации реализации маппера.
 * Основная задача — преобразование данных из сущности {@link Truck} в DTO {@link TruckDto}.
 *
 * Поля:
 * <ul>
 *     <li>{@code INSTANCE} — статическая переменная, содержащая единственный экземпляр маппера, создаваемый MapStruct.</li>
 * </ul>
 *
 * Методы:
 * <ul>
 *     <li>{@link #getTruckDto(Truck)} — преобразует сущность {@link Truck} в DTO {@link TruckDto}.</li>
 * </ul>
 *
 * Использует:
 * <ul>
 *     <li>Аннотацию {@link Mapper} для указания MapStruct о необходимости создания реализации маппера.</li>
 * </ul>
 */
@Mapper
public interface TruckMapper {
    TruckMapper INSTANCE = Mappers.getMapper(TruckMapper.class);
    Truck getTruck(TruckDto truck);
    TruckDto getTruckDto(Truck truckDto);
}
