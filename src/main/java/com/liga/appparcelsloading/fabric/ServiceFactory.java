package com.liga.appparcelsloading.fabric;

import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.service.TruckPrinterService;

public class ServiceFactory {
    public ParcelLoaderService createParcelLoaderService() {
        return new ParcelLoaderService();
    }

    public TruckFactoryService createTruckFactoryService() {
        return new TruckFactoryService();
    }

   public TruckPrinterService createTruckPrinterService(){
       return new TruckPrinterService();
   }
}
