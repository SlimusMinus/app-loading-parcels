package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.service.ParcelService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;

@Controller
@ShellComponent
@AllArgsConstructor
public class ParcelController {
    private final ParcelService parcelService;

    @ShellMethod(value = "Создать или обновить посылку", key = "save-parcel")
    public void save(String name, char symbol, int weight, String orientation) {
        parcelService.save(name, symbol, weight, orientation);
    }

    @ShellMethod(value = "Получить посылку по имени", key = "get-parcel")
    public void showParcelByName(String name) {
        parcelService.getParcelByName(name);
    }

    @ShellMethod(value = "Получить все посылки", key = "get-all-parcels")
    public void showAll() {
        parcelService.getAllParcels();
    }

    @ShellMethod(value = "Удалить посылку", key = "delete-parcel")
    public void delete(String name) {
        parcelService.deleteParcel(name);
    }
}
