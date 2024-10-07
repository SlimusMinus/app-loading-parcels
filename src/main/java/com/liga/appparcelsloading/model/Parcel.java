package com.liga.appparcelsloading.model;

import com.liga.appparcelsloading.converter.IntArrayToJsonConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс сущности {@code Parcel}, представляющий посылку.
 *
 * Маппится на таблицу {@code parcels} в базе данных. Используется для хранения информации о посылках, включая:
 * <ul>
 *     <li>{@code parcelId} — уникальный идентификатор посылки, автоматически генерируемый.</li>
 *     <li>{@code name} — название посылки.</li>
 *     <li>{@code symbol} — символ, представляющий посылку.</li>
 *     <li>{@code form} — форма посылки, хранимая в виде массива {@code int[][]}, который конвертируется в формат JSONB с использованием {@link IntArrayToJsonConverter}.</li>
 * </ul>
 *
 * Аннотации:
 * <ul>
 *     <li>{@link Entity} — указывает, что класс является сущностью JPA.</li>
 *     <li>{@link Table} — задает имя таблицы в базе данных.</li>
 *     <li>{@link Id} — указывает, что поле {@code parcelId} является первичным ключом.</li>
 *     <li>{@link GeneratedValue} — определяет стратегию генерации значения первичного ключа.</li>
 *     <li>{@link Column} — задает параметры маппинга для полей базы данных.</li>
 *     <li>{@link Convert} — использует конвертер для преобразования массива {@code int[][]} в формат JSONB.</li>
 *     <li>{@link Data}, {@link AllArgsConstructor}, {@link NoArgsConstructor} — аннотации Lombok для генерации конструктора, геттеров, сеттеров, методов {@code equals()} и {@code hashCode()}.</li>
 * </ul>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parcels")
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int parcelId;
    private String name;
    private char symbol;
    @Convert(converter = IntArrayToJsonConverter.class)
    @Column(columnDefinition = "jsonb")
    private int[][] form;

    public Parcel(int[][] form) {
        this.form = form;
    }

    public Parcel(String name, char symbol, int[][] form) {
        this.name = name;
        this.symbol = symbol;
        this.form = form;
    }
}
