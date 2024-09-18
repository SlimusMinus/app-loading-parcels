package com.liga.loadingParcelsApp.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.loadingParcelsApp.model.Truck;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonFileReader {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * Читает данные из JSON файла и выводит информацию о каждом грузовике
     * и его посылках в консоль.
     *
     * @param fileName путь к файлу, который необходимо прочитать
     */
    public List<Truck> read(String fileName) {
        List<Truck> trucks = new ArrayList<>();
        final int PARCEL_INCREMENT = 1;
        log.info("Начало чтения файла {}", fileName);
        try {
            trucks = OBJECT_MAPPER.readValue(new File(fileName), new TypeReference<>() {});
            log.info("Файл успешно прочитан: {}", fileName);
            for (Truck truck : trucks) {
                System.out.println("Грузовик " + truck.getName() + " содержит");
                Map<Integer, Integer> parcels = new HashMap<>();
                final List<Integer> truckParcels = truck.getParcels();
                for (Integer parcel : truckParcels) {
                    parcels.merge(parcel, PARCEL_INCREMENT, Integer::sum);
                }
                parcels.forEach((size, count) ->
                        System.out.println(count + " посылки(у) размером " + size)
                );
            }
        } catch (IOException e) {
            log.error("Ошибка чтения файла {}: {}", fileName, e.getMessage());
        }
        return trucks;
    }
}
