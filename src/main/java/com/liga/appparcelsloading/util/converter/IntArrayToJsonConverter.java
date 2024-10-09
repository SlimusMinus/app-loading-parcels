package com.liga.appparcelsloading.util.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Конвертер для преобразования массива целых чисел int[][] в строку JSON и обратно.
 *
 * Этот класс используется для автоматического преобразования данных между
 * Java-объектами и базой данных при использовании JPA. Конвертер сериализует
 * массив целых чисел в формат JSON для хранения в базе данных и десериализует JSON обратно
 * в массив при чтении данных.
 *
 * Аннотация {@link Converter} с параметром {@code autoApply = true} позволяет автоматически
 * применять этот конвертер ко всем полям, имеющим тип int[][].
 *
 * Использует Jackson {@link ObjectMapper} для выполнения операций сериализации и десериализации.
 * Логирование операций осуществляется с помощью {@link Slf4j}.
 */
@Converter(autoApply = true)
@AllArgsConstructor
@Component
@Slf4j
public class IntArrayToJsonConverter implements AttributeConverter<int[][], String> {

    private final ObjectMapper objectMapper;

    /**
     * Конвертирует массив целых чисел int[][] в строку JSON для сохранения в базе данных.
     *
     * @param attribute Массив int[][] для преобразования.
     * @return JSON-строка, представляющая массив int[][].
     * @throws IllegalArgumentException Если произошла ошибка при сериализации массива.
     */
    @Override
    public String convertToDatabaseColumn(int[][] attribute) {
        try {
            String json = objectMapper.writeValueAsString(attribute);
            log.debug("Успешная сериализация массива int[][] в JSON: {}", json);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации массива int[][] в JSON: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Ошибка сериализации массива", e);
        }
    }

    /**
     * Конвертирует строку JSON обратно в массив целых чисел int[][] при чтении из базы данных.
     *
     * @param dbData JSON-строка, представляющая массив int[][].
     * @return Массив int[][], полученный из JSON-строки.
     * @throws IllegalArgumentException Если произошла ошибка при десериализации JSON-строки.
     */
    @Override
    public int[][] convertToEntityAttribute(String dbData) {
        try {
            int[][] result = objectMapper.readValue(dbData, int[][].class);
            log.debug("Успешная десериализация JSON в массив int[][]: {}", (Object) result);
            return result;
        } catch (IOException e) {
            log.error("Ошибка десериализации JSON в массив int[][]: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Ошибка десериализации JSON в массив", e);
        }
    }
}