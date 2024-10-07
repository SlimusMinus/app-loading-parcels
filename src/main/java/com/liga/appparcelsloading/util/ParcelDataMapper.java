package com.liga.appparcelsloading.util;

import com.liga.appparcelsloading.model.Parcel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для отображения и преобразования данных о посылках.
 * Предоставляет методы для получения всех посылок и их форм.
 */
@Slf4j
@Service
@Getter
public class ParcelDataMapper {
    private final List<String> names = List.of("Телевизор", "Кофемашина", "Чайник", "Холодильник", "Плита", "Приставка", "Комбайн", "Пылесос", "Телефон", "Наушники");
    private final Map<Integer, Character> symbols = Map.of(
            1, '!',
            2, '@',
            3, '#',
            4, '$',
            5, '%',
            6, '^',
            7, '&',
            8, '*',
            9, '+'
    );

    /**
     * Получает все посылки из файла и сопоставляет имена и символы.
     *
     * @return Карта, где ключом является имя посылки, а значением - объект Parcel.
     */
    public Map<String, Parcel> getAllParcels() {
        log.info("Начало загрузки всех посылок.");
        int counter = 0;
        Map<String, Parcel> parcels = new HashMap<>();
        for (Parcel parcel : loadAllParcels()) {
            Parcel newParcel = new Parcel();
            String name = names.get(counter);
            counter++;
            newParcel.setName(name);
            char symbol = symbols.get(parcel.getForm()[0][0]);
            newParcel.setSymbol(symbol);
            newParcel.setForm(parcel.getForm());
            parcels.put(newParcel.getName(), newParcel);
        }
        log.info("Загрузка всех посылок завершена. Количество загруженных посылок: {}", parcels.size());
        return parcels;
    }

    /**
     * Преобразует содержимое посылки в двумерный массив символов.
     * Для каждой ячейки массива, соответствующей содержимому посылки, заполняется символом посылки.
     *
     * @param parcel        объект {@link Parcel}, содержащий информацию о посылке, включая символ
     * @param parcelContent двумерный массив {@code int[][]}, представляющий содержимое посылки
     *                      (например, матрицу формы посылки)
     * @return двумерный массив символов {@code char[][]}, где каждый элемент соответствует
     * символу посылки на соответствующем месте
     */
    public char[][] getSymbolParcels(Parcel parcel, int[][] parcelContent) {
        int numRows = parcelContent.length;
        int numCols = parcelContent[0].length;
        char[][] symbolParcels = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                symbolParcels[i][j] = parcel.getSymbol();
            }
        }
        return symbolParcels;
    }

    /**
     * Устанавливает горизонтальную форму посылки в зависимости от веса.
     *
     * @param weight Вес посылки.
     * @return Двумерный массив, представляющий горизонтальную форму посылки.
     */
    public int[][] setHorizontalForm(int weight) {
        log.debug("Установка горизонтальной формы для веса: {}", weight);
        return switch (weight) {
            case 1 -> new int[][]{{1}};
            case 2 -> new int[][]{{2, 2}};
            case 3 -> new int[][]{{3, 3, 3}};
            case 4 -> new int[][]{{4, 4, 4, 4}};
            case 5 -> new int[][]{{5, 5, 5, 5, 5}};
            case 6 -> new int[][]{{6, 6, 6}, {6, 6, 6}};
            case 7 -> new int[][]{{7, 7, 7}, {7, 7, 7, 7}};
            case 8 -> new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}};
            case 9 -> new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};
            default -> new int[][]{};
        };
    }

    /**
     * Устанавливает вертикальную форму посылки в зависимости от веса.
     *
     * @param weight Вес посылки.
     * @return Двумерный массив, представляющий вертикальную форму посылки.
     */
    public int[][] setVerticalForm(int weight) {
        log.debug("Установка вертикальной формы для веса: {}", weight);
        return switch (weight) {
            case 1 -> new int[][]{{1}};
            case 2 -> new int[][]{{2}, {2}};
            case 3 -> new int[][]{{3}, {3}, {3}};
            case 4 -> new int[][]{{4}, {4}, {4}, {4}};
            case 5 -> new int[][]{{5}, {5}, {5}, {5}, {5}};
            case 6 -> new int[][]{{6, 6}, {6, 6}, {6, 6}};
            case 7 -> new int[][]{{7, 7}, {7, 7}, {7, 7}, {7}};
            case 8 -> new int[][]{{8, 8}, {8, 8}, {8, 8}, {8, 8}};
            case 9 -> new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};
            default -> new int[][]{};
        };
    }

    private List<Parcel> loadAllParcels() {
        log.debug("Загрузка посылок из файла parcels.txt.");
        FileReader fileReader = new FileReader();
        return fileReader.getAllParcels("parcels.txt");
    }
}
