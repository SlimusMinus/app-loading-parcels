package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Package;
import com.liga.loadingParcelsApp.util.FileReader;

import java.util.List;

public class TruckService {
    public static void startLoading(){
        FileReader fileReader = new FileReader();
        List<Package> packages = fileReader.getAllPackages("parcels.txt");
        LoadingTrucks loadingTrucks = new LoadingTrucks();
        if (ValidationParcels.isValidation(packages)) {
            List<char[][]> trucks = loadingTrucks.packPackages(packages);
            loadingTrucks.printTrucks(trucks);
        }
    }
}
