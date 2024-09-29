package com.liga.appparcelsloading.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.DefaultParcelRepository;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.service.ParcelLoaderService;
import com.liga.appparcelsloading.service.ParcelService;
import com.liga.appparcelsloading.service.TruckFactoryService;
import com.liga.appparcelsloading.service.TruckPrinterService;
import com.liga.appparcelsloading.util.JsonFileReader;
import com.liga.appparcelsloading.util.ParcelMapper;
import com.liga.appparcelsloading.validator.ParcelValidator;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {
    @Bean
    public JsonFileReader jsonFileReader() {
        return new JsonFileReader(objectMapper());
    }

    @Bean
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ParcelLoaderService parcelLoaderService() {
        return new ParcelLoaderService();
    }

    @Bean
    public TruckFactoryService truckFactoryService() {
        return new TruckFactoryService();
    }

    @Bean
    public TruckPrinterService truckPrinterService() {
        return new TruckPrinterService();
    }

    @Bean
    public TruckCountValidate truckCountValidator() {
        return new TruckCountValidate();
    }

    @Bean
    public ParcelValidator parcelValidator() {
        return new ParcelValidator();
    }

    @Bean
    public DefaultParcelRepository defaultParcelRepository() {
        return new DefaultParcelRepository(parcelMapper());
    }

    @Bean
    public ParcelMapper parcelMapper() {
        return new ParcelMapper();
    }

    @Bean
    public ParcelService parcelService() {
        return new ParcelService(defaultParcelRepository());
    }

}
