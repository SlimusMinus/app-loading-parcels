package com.liga.appparcelsloading.util;

import com.liga.appparcelsloading.model.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParcelMapper {

    private final List<String> names = List.of("Телевизор", "Кофемашина", "Чайник", "Холодильник", "Плита", "Приставка", "Комбайн", "Пылесос", "Телефон", "Наушники");
    private int counter = 0;
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

    public Map<String, Parcel> getAllParcels() {
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
        return parcels;
    }

    private List<Parcel> loadAllParcels() {
        FileReader fileReader = new FileReader();
        return fileReader.getAllParcels("parcels.txt");
    }

    public int[][] setWeight(int weight) {
        return switch (weight){
            case 1 -> new int[][]{{1}};
            case 2 -> new int[][]{{2,2}};
            case 3 -> new int[][]{{3,3,3}};
            case 4 -> new int[][]{{4,4,4,4}};
            case 5 -> new int[][]{{5,5,5,5,5}};
            case 6 -> new int[][]{{6,6,6},{6,6,6}};
            case 7 -> new int[][]{{7,7,7},{7,7,7,7}};
            case 8 -> new int[][]{{8,8,8,8},{8,8,8,8}};
            case 9 -> new int[][]{{9,9,9},{9,9,9},{9,9,9}};
            default -> new int[][]{};
        };
    }
}
