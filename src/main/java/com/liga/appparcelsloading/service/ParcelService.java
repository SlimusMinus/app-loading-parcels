package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

/**
 * Сервис для управления посылками.
 * Предоставляет методы для создания, обновления, получения и удаления посылок.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ParcelService {
    private final ParcelRepository repository;
    private final ParcelMapper parcelMapper;

    public void save(String name, char symbol, int weight, String orientation) {
        Optional<Parcel> existingParcel = getParcel(name);
        Parcel parcel = existingParcel.orElse(new Parcel());
        saveParcel(parcel, name, symbol, weight, orientation);
        if (existingParcel.isPresent()) {
            log.info("Посылка '{}' успешно обновлена", parcel.getName());
        } else {
            log.info("Посылка '{}' успешно создана", parcel.getName());
        }
    }

    public void getParcelByName(String name) {
        Optional<Parcel> parcel = getParcel(name);
        System.out.println(parcel);
    }

    public void getAllParcels() {
        repository.findAll().forEach(System.out::println);
    }

    public void deleteParcel(String name) {
        repository.delete(name);
    }

    private Optional<Parcel> getParcel(String nameParcel) {
        return repository.findByName(nameParcel);
    }

    private void saveParcel(Parcel parcel, String name, char symbol, int weight, String orientation) {
        parcel.setName(name);
        parcel.setSymbol(symbol);
        int[][] form;
        switch (orientation) {
            case "1": {
                form = parcelMapper.setVerticalForm(weight);
            }
            break;
            case "2": {
                form = parcelMapper.setHorizontalForm(weight);
            }
            break;
            default:
                form = parcelMapper.setHorizontalForm(weight);
        }
        parcel.setForm(form);
        repository.save(parcel);
    }
}
