package com.liga.appparcelsloading.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.repository.DefaultParcelRepository;
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

import java.util.Scanner;

/**
 * Класс конфигурации Spring, содержащий определения бинов для приложения загрузки посылок в грузовики.
 * Все бины создаются и управляются Spring контекстом.
 */
@Configuration
public class Config {
    /**
     * Создает бин {@link JsonFileReader}, который используется для чтения данных из JSON-файлов.
     *
     * @return экземпляр {@link JsonFileReader}
     */
    @Bean
    public JsonFileReader jsonFileReader() {
        return new JsonFileReader(objectMapper());
    }

    /**
     * Создает бин {@link ObjectMapper}, используемый для сериализации и десериализации JSON-данных.
     *
     * @return экземпляр {@link ObjectMapper}
     */
    @Bean
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Создает бин {@link ParcelLoaderService}, который управляет процессом загрузки посылок в грузовики.
     *
     * @return экземпляр {@link ParcelLoaderService}
     */
    @Bean
    public ParcelLoaderService parcelLoaderService() {
        return new ParcelLoaderService();
    }

    /**
     * Создает бин {@link TruckFactoryService}, который отвечает за создание грузовиков.
     *
     * @return экземпляр {@link TruckFactoryService}
     */
    @Bean
    public TruckFactoryService truckFactoryService() {
        return new TruckFactoryService();
    }

    /**
     * Создает бин {@link TruckPrinterService}, который используется для вывода информации о грузовиках.
     *
     * @return экземпляр {@link TruckPrinterService}
     */
    @Bean
    public TruckPrinterService truckPrinterService() {
        return new TruckPrinterService();
    }

    /**
     * Создает бин {@link TruckCountValidate}, который выполняет валидацию количества грузовиков.
     *
     * @return экземпляр {@link TruckCountValidate}
     */
    @Bean
    public TruckCountValidate truckCountValidator() {
        return new TruckCountValidate();
    }

    /**
     * Создает бин {@link ParcelValidator}, который выполняет валидацию посылок.
     *
     * @return экземпляр {@link ParcelValidator}
     */
    @Bean
    public ParcelValidator parcelValidator() {
        return new ParcelValidator();
    }

    /**
     * Создает бин {@link DefaultParcelRepository}, который управляет посылками с помощью хранилища.
     *
     * @return экземпляр {@link DefaultParcelRepository}
     */
    @Bean
    public DefaultParcelRepository defaultParcelRepository() {
        return new DefaultParcelRepository(parcelMapper());
    }

    /**
     * Создает бин {@link ParcelMapper}, который используется для преобразования данных о посылках.
     *
     * @return экземпляр {@link ParcelMapper}
     */
    @Bean
    public ParcelMapper parcelMapper() {
        return new ParcelMapper();
    }

    /**
     * Создает бин {@link ParcelService}, который управляет операциями с посылками.
     *
     * @return экземпляр {@link ParcelService}
     */
    @Bean
    public ParcelService parcelService() {
        return new ParcelService(defaultParcelRepository(), parcelMapper(), scanner());
    }

    /**
     * Создает бин {@link Scanner}, используемый для считывания пользовательского ввода из консоли.
     *
     * @return экземпляр {@link Scanner}
     */
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

}
