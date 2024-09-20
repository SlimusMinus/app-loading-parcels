package com.liga.appparcelsloading;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.FileReader;
import com.liga.appparcelsloading.util.JsonFileReader;
import com.liga.appparcelsloading.validator.ParcelValidator;

import java.util.List;

public class TruckLoadingApplication {
    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        final List<Parcel> parcels = fileReader.getAllParcels("parcels.txt");
        ParcelValidator parcelValidator = new ParcelValidator();
        JsonFileReader jsonFileReader = new JsonFileReader();
        TruckPrinterService truckPrinterService = new TruckPrinterService();

        ManagerApp managerApp = new ManagerApp(parcels, parcelValidator, jsonFileReader, truckPrinterService);
        managerApp.startLoading();
    }
}


