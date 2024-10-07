package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelInMemoryService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

/**
 * Класс ParcelInMemoryController представляет собой контроллер для управления посылками в памяти.
 * Предоставляет методы для создания, обновления, получения и удаления посылок через интерфейс Spring Shell.
 */
@Controller
@ShellComponent
@AllArgsConstructor
public class ParcelInMemoryController {
    private final ParcelInMemoryService parcelInMemoryService;

    /**
     * Создает или обновляет посылку.
     *
     * @param name        имя посылки.
     * @param symbol      символ, представляющий посылку.
     * @param weight      вес посылки.
     * @param orientation ориентация посылки.
     * @return объект созданной или обновленной посылки.
     */
    @ShellMethod(value = "Создать или обновить посылку", key = "save-parcel")
    public Parcel save(String name, char symbol, int weight, String orientation) {
        return parcelInMemoryService.save(name, symbol, weight, orientation);
    }

    /**
     * Получает посылку по имени.
     *
     * @param name имя посылки.
     * @return объект Optional, содержащий посылку, если она найдена.
     */
    @ShellMethod(value = "Получить посылку по имени", key = "get-parcel")
    public Optional<Parcel> showParcelByName(String name) {
        return parcelInMemoryService.getParcelByName(name);
    }

    /**
     * Получает список всех посылок.
     *
     * @return список всех посылок, хранящихся в памяти.
     */
    @ShellMethod(value = "Получить все посылки", key = "get-all-parcels")
    public List<Parcel> showAll() {
        return parcelInMemoryService.getAllParcels();
    }

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки для удаления.
     * @return true, если посылка успешно удалена; false, если посылка не найдена.
     */
    @ShellMethod(value = "Удалить посылку", key = "delete-parcel")
    public boolean delete(String name) {
        return parcelInMemoryService.deleteParcel(name);
    }
}
