package com.liga.appparcelsloading;

import com.liga.appparcelsloading.fabric.JsonFileReaderFactory;
import com.liga.appparcelsloading.fabric.ServiceFactory;
import com.liga.appparcelsloading.fabric.ValidateFactory;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.FileReader;
import com.liga.appparcelsloading.util.JsonFileReader;
import com.liga.appparcelsloading.validator.ParcelValidator;

import java.util.List;

public class TruckLoadingApplication {
    private static final ServiceFactory factory = new ServiceFactory();
    private static final ValidateFactory validateFactory = new ValidateFactory();
    private static final JsonFileReaderFactory jsonReaderFactory = new JsonFileReaderFactory();

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        final List<Parcel> parcels = fileReader.getAllParcels("parcels.txt");

        JsonFileReader jsonFileReader = jsonReaderFactory.createJsonFileReader();
        ParcelValidator parcelValidator = validateFactory.createParcelValidator();
        TruckPrinterService truckPrinterService = factory.createTruckPrinterService();

        ManagerApp managerApp = new ManagerApp(parcels, parcelValidator, jsonFileReader, truckPrinterService);
        managerApp.startLoading();
    }
}


