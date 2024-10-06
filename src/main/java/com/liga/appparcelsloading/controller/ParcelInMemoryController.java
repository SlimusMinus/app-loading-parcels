package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelServiceInMemory;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@ShellComponent
@AllArgsConstructor
public class ParcelInMemoryController {
    private final ParcelServiceInMemory parcelServiceInMemory;

    @ShellMethod(value = "Создать или обновить посылку", key = "save-parcel")
    public Parcel save(String name, char symbol, int weight, String orientation) {
        return parcelServiceInMemory.save(name, symbol, weight, orientation);
    }

    @ShellMethod(value = "Получить посылку по имени", key = "get-parcel")
    public Optional<Parcel> showParcelByName(String name) {
        return parcelServiceInMemory.getParcelByName(name);
    }

    @ShellMethod(value = "Получить все посылки", key = "get-all-parcels")
    public List<Parcel> showAll() {
        return parcelServiceInMemory.getAllParcels();
    }

    @ShellMethod(value = "Удалить посылку", key = "delete-parcel")
    public boolean delete(String name) {
        return parcelServiceInMemory.deleteParcel(name);
    }
}
