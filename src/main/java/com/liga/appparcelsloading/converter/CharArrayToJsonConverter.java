package com.liga.appparcelsloading.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Converter(autoApply = true)
@Component
@AllArgsConstructor
@Slf4j
public class CharArrayToJsonConverter implements AttributeConverter<char[][], String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(char[][] attribute) {
        try {
            String json = objectMapper.writeValueAsString(attribute);
            log.debug("Успешная сериализация массива char[][] в JSON: {}", json);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализации массива char[][] в JSON: {}", Arrays.deepToString(attribute), e);
            throw new IllegalArgumentException("Ошибка при сериализации массива char[][] в JSON", e);
        }
    }

    @Override
    public char[][] convertToEntityAttribute(String dbData) {
        try {
            char[][] array = objectMapper.readValue(dbData, char[][].class);
            log.debug("Успешная десериализация JSON в массив char[][]: {}", Arrays.deepToString(array));
            return array;
        } catch (IOException e) {
            log.error("Ошибка при десериализации JSON в массив char[][]: {}", dbData, e);
            throw new IllegalArgumentException("Ошибка при десериализации JSON в массив char[][]", e);
        }
    }
}
