package com.liga.appparcelsloading.truck.model;

import com.liga.appparcelsloading.converter.CharArrayToJsonConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс сущности {@code Truck}, представляющий грузовик.
 *
 * Маппится на таблицу {@code trucks} в базе данных. Используется для хранения информации о грузовиках, включая:
 * <ul>
 *     <li>{@code id} — уникальный идентификатор грузовика, автоматически генерируемый.</li>
 *     <li>{@code nameTruck} — название грузовика.</li>
 *     <li>{@code nameParcels} — название посылок, загруженных в грузовик.</li>
 *     <li>{@code parcels} — массив символов {@code char[][]}, представляющий загруженные посылки, который конвертируется в формат JSON с использованием {@link CharArrayToJsonConverter}.</li>
 * </ul>
 *
 * Аннотации:
 * <ul>
 *     <li>{@link Entity} — указывает, что класс является сущностью JPA.</li>
 *     <li>{@link Table} — задает имя таблицы в базе данных.</li>
 *     <li>{@link Id} — указывает, что поле {@code id} является первичным ключом.</li>
 *     <li>{@link GeneratedValue} — определяет стратегию генерации значения первичного ключа.</li>
 *     <li>{@link Column} — задает параметры маппинга для полей базы данных.</li>
 *     <li>{@link Convert} — использует конвертер для преобразования массива {@code char[][]} в формат JSON.</li>
 *     <li>{@link Data}, {@link AllArgsConstructor}, {@link NoArgsConstructor} — аннотации Lombok для генерации конструктора, геттеров, сеттеров, методов {@code equals()} и {@code hashCode()}.</li>
 * </ul>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trucks")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name_truck")
    private String nameTruck;
    @Column(name = "name_parcels")
    private String nameParcels;
    @Convert(converter = CharArrayToJsonConverter.class)
    @Column(columnDefinition = "json")
    private char[][] parcels;

    public Truck(String nameTruck, String nameParcels, char[][] parcels) {
        this.nameTruck = nameTruck;
        this.nameParcels = nameParcels;
        this.parcels = parcels;
    }
}
