package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelRepository;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ParcelService {
    private ParcelRepository repository;

    public ParcelService(ParcelRepository repository) {
        this.repository = repository;
    }

    public void parcelsManager(){
        System.out.println("""
                        Выберите действие:
                        1 - создать посылку
                        2 - обновить посылку
                        3 - получить посылку по ее имени
                        4 - получить все посылки
                        5 - удалить посылку
                        """);
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        switch (choice) {
            case "1": {
                Parcel parcel = new Parcel();
                System.out.println("Введите название для посылки");
                String name = scanner.next();
                parcel.setName(name);
                System.out.println("Введите символ которым будет обозначаться посылка");
                char symbol = scanner.next().charAt(0);
                parcel.setSymbol(symbol);
                System.out.println("Введите вес вашей посылки");
                int price = scanner.nextInt();
            }
        }
    }
}
