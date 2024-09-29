package com.liga.appparcelsloading.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.util.JsonFileWriter;
import com.liga.appparcelsloading.util.ParcelMapper;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.liga.appparcelsloading.DataTest.TRUCK_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование класса LoadingTrucks")
class TruckLoadServiceTest {

    private TruckLoadAlgorithm truckLoadService;
    private ParcelLoaderService parcelLoaderService;
    private TruckFactoryService truckFactoryService;
    private JsonFileWriter jsonFileWriter;
    private ParcelMapper parcelMapper;
    private final TruckCountValidate validateTruckCount = new TruckCountValidate();

    @BeforeEach
    void setUp() {
        jsonFileWriter = new JsonFileWriter(new ObjectMapper());
        truckLoadService = new EvenTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter);
        parcelLoaderService = new ParcelLoaderService();
        truckFactoryService = new TruckFactoryService();
        parcelMapper = new ParcelMapper();
    }

    @Test
    @DisplayName("Проверка алгоритма равномерной погрузки")
    void testEvenlyDistributeParcels() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter, parcelMapper);

        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}})
        );
        List<char[][]> distributeParcels = truckLoadService.loadParcels(parcels, 2, 6);
        assertThat(distributeParcels.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Проверка алгоритма равномерной погрузки на выброс исключения")
    void testEvenlyDistributeParcelsException() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter, parcelMapper);
        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}})
        );
        assertThatThrownBy(() -> truckLoadService.loadParcels(parcels, 1, 6)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Проверка, что метод создает пустой кузов правильного размера, заполненный пробелами")
    void testCreateEmptyTruck() {
        char[][] emptyTruck = truckFactoryService.createEmptyTruck(TRUCK_SIZE);
        assertThat(TRUCK_SIZE).isEqualTo(emptyTruck.length);
        assertThat(TRUCK_SIZE).isEqualTo(emptyTruck[0].length);
        for (char[] row : emptyTruck) {
            for (char cell : row) {
                assertThat(' ').isEqualTo(cell);
            }
        }
    }

    @Test
    @DisplayName("Проверка, что посылка успешно помещается.")
    void testPlaceParcelValid() {
        char[][] truck = truckFactoryService.createEmptyTruck(TRUCK_SIZE);
        char[][] pack = {
                {'2', '2'}
        };
        parcelLoaderService.placeParcels(truck, pack, TRUCK_SIZE);
        assertThat(parcelLoaderService.placeParcels(truck, pack, TRUCK_SIZE)).isTrue();
        assertThat('2').isEqualTo(truck[5][0]);
        assertThat('2').isEqualTo(truck[5][1]);
    }

    @Test
    @DisplayName("Проверка, что нельзя поместить вторую посылку на уже занятую область")
    void testPlaceParcelInvalid() {
        char[][] truck = truckFactoryService.createEmptyTruck(TRUCK_SIZE);
        char[][] pack1 = {
                {3, 3, 3}
        };
        char[][] pack2 = {
                {4, 4, 4}
        };
        parcelLoaderService.placeParcels(truck, pack1, TRUCK_SIZE);
        assertThat(parcelLoaderService.placeParcels(truck, pack2, TRUCK_SIZE)).isTrue();
    }

    @Test
    @DisplayName("Проверка выброса исключения при недостаточном количестве грузовиков")
    void testPlaceParcelException() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter, parcelMapper);
        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{1, 1}, {1, 1}}),
                new Parcel(new int[][]{{2, 2, 2}, {2, 2, 2}}),
                new Parcel(new int[][]{{3, 3, 3, 3}, {3, 3, 3, 3}})
        );
        assertThatThrownBy(() -> truckLoadService.loadParcels(parcels, 0, 6)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Проверка процесса упаковки по именам нескольких посылок в кузовы.")
    void testLoadParcelsByName() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter, parcelMapper);
        String names = "Кофемашина, Чайник";
        List<char[][]> parcelsByName = truckLoadService.loadParcelsByName(names, 5, 6);
        assertThat(parcelsByName.get(0)[5][0]).isEqualTo('^');
        assertThat(parcelsByName.get(0)[3][0]).isEqualTo('%');

    }

}