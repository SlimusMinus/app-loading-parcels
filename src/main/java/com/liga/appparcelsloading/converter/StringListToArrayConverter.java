package com.liga.appparcelsloading.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Converter(autoApply = true)
@Component
@Slf4j
public class StringListToArrayConverter implements AttributeConverter<List<String>, String[]> {

    @Override
    public String[] convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null) {
            log.debug("Список строк (List<String>) является null, возвращается null.");
            return null;
        }
        String[] array = attribute.toArray(new String[0]);
        log.debug("Успешная конвертация списка строк (List<String>) в массив: {}", Arrays.toString(array));
        return array;
    }

    @Override
    public List<String> convertToEntityAttribute(String[] dbData) {
        if (dbData == null) {
            log.debug("Массив строк (String[]) является null, возвращается null.");
            return null;
        }
        List<String> list = Arrays.asList(dbData);
        log.debug("Успешная конвертация массива строк (String[]) в список: {}", list);
        return list;
    }
}
