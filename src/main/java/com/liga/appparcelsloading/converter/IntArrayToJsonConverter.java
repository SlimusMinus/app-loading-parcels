package com.liga.appparcelsloading.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Converter(autoApply = true)
@AllArgsConstructor
@Component
@Slf4j
public class IntArrayToJsonConverter implements AttributeConverter<int[][], String> {

    private final ObjectMapper objectMapper;

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