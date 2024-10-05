package com.liga.appparcelsloading.validator;

import com.liga.appparcelsloading.model.Parcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для проверки валидности посылок.
 * Проверяет, соответствуют ли посылки заранее определенным шаблонам по содержимому.
 */
@Slf4j
@Component
public class ParcelValidator {

    /**
     * Проверяет, что каждое число в посылке содержится необходимое количество раз.
     * Логирует информацию о начале и результате проверки, а также предупреждения о несоответствиях.
     *
     * @param parcels список посылок для проверки
     * @return {@code true}, если все посылки соответствуют условиям; {@code false} в противном случае
     */
    public boolean isValid(List<Parcel> parcels) {
        log.info("Начало проверки валидности посылок. Количество посылок: {}", parcels.size());

        for (Parcel parcel : parcels) {
            int[][] content = parcel.getForm();

            Map<Integer, Integer> numberCounts = countNumbers(content);
            if (!isValidParcel(numberCounts)) {
                log.warn("Посылка с содержимым {} не прошла проверку.", Arrays.deepToString(content));
                return false;
            }
        }

        log.info("Все посылки прошли проверку.");
        return true;
    }

    /**
     * Подсчитывает количество вхождений каждого числа в посылке.
     *
     * @param content содержимое посылки
     * @return карта с количеством вхождений каждого числа
     */
    private Map<Integer, Integer> countNumbers(int[][] content) {
        Map<Integer, Integer> numberCounts = new HashMap<>();
        final int firstNumber = 0;
        final int increment = 1;
        for (int[] row : content) {
            for (int number : row) {
                numberCounts.put(number, numberCounts.getOrDefault(number, firstNumber) + increment);
            }
        }
        return numberCounts;
    }

    /**
     * Проверяет, что каждое число содержится необходимое количество раз.
     * Здесь можно настроить правила для каждого числа.
     *
     * @param numberCounts карта с количеством вхождений каждого числа
     * @return {@code true}, если посылка валидна; {@code false} в противном случае
     */
    private boolean isValidParcel(Map<Integer, Integer> numberCounts) {
        for (Map.Entry<Integer, Integer> entry : numberCounts.entrySet()) {
            int number = entry.getKey();
            int count = entry.getValue();
            if (count != number) {
                return false;
            }
        }
        return true;
    }
}
