package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Package;
import com.liga.loadingParcelsApp.util.FileReader;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class TruckService {
    public static void startLoading(){
        log.info("Начало процесса загрузки посылок.");
        FileReader fileReader = new FileReader();
        List<Package> packages = fileReader.getAllPackages("parcels.txt");
        LoadingTrucks loadingTrucks = new LoadingTrucks();

        if (ValidationParcels.isValidation(packages)) {
            log .info("Посылки прошли проверку на валидность.");
            List<char[][]> trucks = loadingTrucks.packPackages(packages, 1);
            log.info("Успешно упаковано {} грузовиков.", trucks.size());
            WriteTrucksInMemory.writeTrucks();
            loadingTrucks.printTrucks(trucks);
        }
    }
}
