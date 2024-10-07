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

/**
 * Конвертер для преобразования массива символов char[][] в строку JSON и обратно.
 *
 * Этот класс используется для автоматического преобразования данных между
 * Java-объектами и базой данных при использовании JPA. Конвертер сериализует
 * массив символов в формат JSON для хранения в базе данных и десериализует JSON обратно
 * в массив при чтении данных.
 *
 * Аннотация {@link Converter} с параметром {@code autoApply = true} означает, что этот конвертер
 * будет автоматически применяться ко всем полям, имеющим тип char[][].
 *
 * Использует Jackson {@link ObjectMapper} для выполнения операций сериализации и десериализации.
 * Логирование операций осуществляется с помощью {@link Slf4j}.
 */
@Converter(autoApply = true)
@Component
@AllArgsConstructor
@Slf4j
public class CharArrayToJsonConverter implements AttributeConverter<char[][], String> {

    private final ObjectMapper objectMapper;

    /**
     * Конвертирует массив символов char[][] в строку JSON для сохранения в базе данных.
     *
     * @param attribute Массив char[][] для преобразования.
     * @return JSON-строка, представляющая массив char[][].
     * @throws IllegalArgumentException Если произошла ошибка при сериализации массива.
     */
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

    /**
     * Конвертирует строку JSON обратно в массив символов char[][] при чтении из базы данных.
     *
     * @param dbData JSON-строка, представляющая массив char[][].
     * @return Массив char[][], полученный из JSON-строки.
     * @throws IllegalArgumentException Если произошла ошибка при десериализации JSON-строки.
     */
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
