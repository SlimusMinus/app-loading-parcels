package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.algorithm.EvenTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.util.JsonFileWriter;
import com.liga.appparcelsloading.validator.TruckCountValidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    private final TruckCountValidate validateTruckCount = new TruckCountValidate();

    @BeforeEach
    void setUp() {
        truckLoadService = new EvenTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter);
        parcelLoaderService = new ParcelLoaderService();
        truckFactoryService = new TruckFactoryService();
    }

    @Test
    @DisplayName("Проверка алгоритма равномерной погрузки")
    void testEvenlyDistributeParcels() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter);

        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}})
        );
        final List<char[][]> distributeParcels = truckLoadService.loadParcels(parcels, 2);
        assertThat(distributeParcels.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Проверка алгоритма равномерной погрузки на выброс исключения")
    void testEvenlyDistributeParcelsException() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter);
        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}})
        );
        assertThatThrownBy(() -> truckLoadService.loadParcels(parcels, 1)).isInstanceOf(IllegalArgumentException.class);
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
        int[][] pack = {
                {2, 2}
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
        int[][] pack1 = {
                {3, 3, 3}
        };
        int[][] pack2 = {
                {4, 4, 4}
        };
        parcelLoaderService.placeParcels(truck, pack1, TRUCK_SIZE);
        assertThat(parcelLoaderService.placeParcels(truck, pack2, TRUCK_SIZE)).isTrue();
    }

    @Test
    @DisplayName("Проверка выброса исключения при недостаточном количестве грузовиков")
    void testPlaceParcelException() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter);
        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{1, 1}, {1, 1}}),
                new Parcel(new int[][]{{2, 2, 2}, {2, 2, 2}}),
                new Parcel(new int[][]{{3, 3, 3, 3}, {3, 3, 3, 3}})
        );
        assertThatThrownBy(() -> truckLoadService.loadParcels(parcels, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Проверка общего процесса упаковки нескольких посылок в кузовы.")
    void testPackParcels() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(truckFactoryService, parcelLoaderService, validateTruckCount, jsonFileWriter);

        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(new int[][]{
                {1, 1},
                {1, 1}
        }));
        parcels.add(new Parcel(new int[][]{
                {2, 2, 2}
        }));
        List<char[][]> trucks = truckLoadService.loadParcels(parcels, 2);
        char[][] truck1 = trucks.get(0);
        assertThat(truck1[5][0]).isEqualTo('1');
        assertThat(truck1[5][1]).isEqualTo('1');
        assertThat(truck1[4][0]).isEqualTo('1');
        assertThat(truck1[4][1]).isEqualTo('1');
        assertThat(truck1[5][2]).isEqualTo('2');
        assertThat(truck1[5][3]).isEqualTo('2');
        assertThat(truck1[5][4]).isEqualTo('2');

    }

}