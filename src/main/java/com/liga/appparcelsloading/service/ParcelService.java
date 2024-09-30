package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Scanner;

/**
 * Сервис для управления посылками.
 * Предоставляет методы для создания, обновления, получения и удаления посылок.
 */
@ShellComponent
@AllArgsConstructor
public class ParcelService {
    private final ParcelRepository repository;
    private final ParcelMapper parcelMapper;
    private final Scanner scanner;

    /**
     * Менеджер посылок. Предоставляет пользователю интерфейс для выбора действий с посылками.
     */
    @ShellMethod(value = "Менеджер посылок", key = "manage-parcels-menu")
    public void showParcelsManagerMenu() {
        System.out.println("""
                Выберите действие:
                create-parcel - создать посылку
                update-parcel - обновить посылку
                get-parcel - получить посылку по ее имени
                get-all-parcels - получить все посылки
                delete-parcel - удалить посылку
                """);
    }

    @ShellMethod(value = "Создать посылку", key = "create-parcel")
    public void createParcel() {
        Parcel newParcel = new Parcel();
        save(newParcel);
    }

    @ShellMethod(value = "Обновить посылку", key = "update-parcel")
    public void updateParcel() {
        Parcel updateParcel = getParcel();
        save(updateParcel);
    }

    @ShellMethod(value = "Получить посылку по имени", key = "get-parcel")
    public void getParcelByName() {
        Parcel parcel = getParcel();
        System.out.println(parcel);
    }

    @ShellMethod(value = "Получить все посылки", key = "get-all-parcels")
    public void getAllParcels() {
        repository.getAll().forEach(System.out::println);
    }

    @ShellMethod(value = "Удалить посылку", key = "delete-parcel")
    public void deleteParcel() {
        System.out.println("Введите название посылки");
        String nameParcel = scanner.next();
        repository.delete(nameParcel);
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
