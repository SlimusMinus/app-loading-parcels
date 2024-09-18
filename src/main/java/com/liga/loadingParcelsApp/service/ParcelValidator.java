package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Parcel;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Класс для проверки валидности посылок.
 * Проверяет, соответствуют ли посылки заранее определенным шаблонам по содержимому.
 */
@Slf4j
public class ParcelValidator {
    private final int[][] NINE = new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};
    private final int[][] EIGHTS = new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}};
    private final int[][] SEVEN = new int[][]{{7, 7, 7}, {7, 7, 7, 7}};
    private final int[][] SIX = new int[][]{{6, 6, 6}, {6, 6, 6}};
    private final int[][] FIVE = new int[][]{{5, 5, 5, 5, 5}};
    private final int[][] FOUR = new int[][]{{4, 4, 4, 4}};
    private final int[][] THREE = new int[][]{{3, 3, 3}};
    private final int[][] TWO = new int[][]{{2, 2}};
    private final int[][] ONE = new int[][]{{1}};

    /**
     * Проверяет, что все посылки в списке соответствуют заранее определенным шаблонам.
     * Логирует информацию о начале и результате проверки, а также предупреждения о несоответствиях.
     *
     * @param parcels список посылок для проверки
     * @return {@code true}, если все посылки соответствуют шаблонам; {@code false} в противном случае
     */
    public boolean isValid(List<Parcel> parcels) {
        log.info("Начало проверки валидности посылок. Количество посылок: {}", parcels.size());
        boolean isValidate;
        final int SIZE_PARCELS = 0;

        for (Parcel parcel : parcels) {
            int[][] content = parcel.getContent();
            isValidate = switch (content[0][0]) {
                case 9 -> Arrays.deepEquals(content, NINE);
                case 8 -> Arrays.deepEquals(content, EIGHTS);
                case 7 -> Arrays.deepEquals(content, SEVEN);
                case 6 -> Arrays.deepEquals(content, SIX);
                case 5 -> Arrays.deepEquals(content, FIVE);
                case 4 -> Arrays.deepEquals(content, FOUR);
                case 3 -> Arrays.deepEquals(content, THREE);
                case 2 -> Arrays.deepEquals(content, TWO);
                case 1 -> Arrays.deepEquals(content, ONE);
                default -> throw new IllegalArgumentException("Некорректное число: " + content[SIZE_PARCELS][SIZE_PARCELS]);
            };
            if (!isValidate) {
                log.warn("Посылка с содержимым {} не прошла проверку.", Arrays.deepToString(content));
                return false;
            }
        }
        log.info("Все посылки прошли проверку.");
        return true;
    }

}
