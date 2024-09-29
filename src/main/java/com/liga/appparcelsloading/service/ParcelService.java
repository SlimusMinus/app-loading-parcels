package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.util.ParcelMapper;

import java.util.Scanner;

public class ParcelService {
    private final ParcelRepository repository;
    private final ParcelMapper parcelMapper;
    private final Scanner scanner;

    public ParcelService(ParcelRepository repository, ParcelMapper parcelMapper, Scanner scanner) {
        this.repository = repository;
        this.parcelMapper = parcelMapper;
        this.scanner = scanner;
    }

    public void parcelsManager() {
        System.out.println("""
                Выберите действие:
                1 - создать посылку
                2 - обновить посылку
                3 - получить посылку по ее имени
                4 - получить все посылки
                5 - удалить посылку
                """);

        String choice = scanner.next();
        switch (choice) {
            case "1": {
                Parcel newParcel = new Parcel();
                save(newParcel);
            }
            break;
            case "2": {
                Parcel updateParcel = getParcel();
                save(updateParcel);
            }
            break;
            case "3": {
                Parcel parcel = getParcel();
                System.out.println(parcel);
            }
            break;
            case "4": {
                System.out.println(repository.getAll().toString());
            }
            break;
            case "5": {
                System.out.println("Введите название посылки");
                String nameParcel = scanner.next();
                repository.delete(nameParcel);
            }
        }
    }

    private Parcel getParcel() {
        System.out.println("Введите название посылки");
        String nameParcel = scanner.next();
        return repository.getByName(nameParcel);
    }

    private void save(Parcel parcel) {
        System.out.println("Введите название для посылки");
        String name = scanner.next();
        parcel.setName(name);
        System.out.println("Введите символ которым будет обозначаться посылка");
        char symbol = scanner.next().charAt(0);
        parcel.setSymbol(symbol);
        System.out.println("Введите вес вашей посылки");
        int price = scanner.nextInt();
        System.out.println("Выберите вариант размещения посылки: 1 - вертикально, 2 - горизонтально");
        String choice = scanner.next();
        int[][] weight;
        switch (choice) {
            case "1": {
                weight = parcelMapper.setVerticalForm(price);
            }
            break;
            case "2": {
                weight = parcelMapper.setHorizontalForm(price);
            }
            break;
            default:
                weight = parcelMapper.setHorizontalForm(price);
        }
        parcel.setForm(weight);
        repository.save(parcel);
    }
}
