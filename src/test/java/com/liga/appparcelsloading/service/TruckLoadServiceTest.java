package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.algorithm.OptimalTruckLoadingAlgorithm;
import com.liga.appparcelsloading.algorithm.TruckLoadAlgorithm;
import com.liga.appparcelsloading.model.Dimension;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.util.ParcelMapper;
import com.liga.appparcelsloading.util.JsonFileWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.liga.appparcelsloading.DataTest.TRUCK_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Тестирование класса LoadingTrucks")
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class TruckLoadServiceTest {

    private TruckLoadAlgorithm truckLoadService;
    @Autowired
    private ParcelLoaderService parcelLoaderService;
    @Autowired
    private TruckFactoryService truckFactoryService;
    @Autowired
    private ParcelMapper parcelMapper;

    @Autowired
    private JsonFileWriter jsonFileWriter;

    @Test
    @DisplayName("Проверка алгоритма равномерной погрузки")
    void testEvenlyDistributeParcels() {
        truckLoadService = new OptimalTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, parcelMapper);
        List<Dimension> dimensions = List.of(new Dimension(6, 6));
        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}})
        );
        List<char[][]> distributeParcels = truckLoadService.loadParcels(parcels, dimensions).stream().map(Truck::getParcels).toList();
        assertThat(distributeParcels.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Проверка алгоритма равномерной погрузки на выброс исключения")
    void testEvenlyDistributeParcelsException() {
        List<Dimension> dimensions = List.of(new Dimension(3, 3));
        truckLoadService = new OptimalTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, parcelMapper);
        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}}),
                new Parcel(new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}})
        );
        assertThatThrownBy(() -> truckLoadService.loadParcels(parcels, dimensions)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("Проверка, что метод создает пустой кузов правильного размера, заполненный пробелами")
    void testCreateEmptyTruck() {
        List<Dimension> dimensions = List.of(new Dimension(6, 6));
        List<char[][]> emptyTrucks = truckFactoryService.createEmptyTruck(dimensions);
        assertThat(TRUCK_SIZE).isEqualTo(emptyTrucks.get(0).length);
        char[][] emptyTruck = emptyTrucks.get(0);
        for (char[] row : emptyTruck) {
            for (char cell : row) {
                assertThat(' ').isEqualTo(cell);
            }
        }
    }

    @Test
    @DisplayName("Проверка, что посылка успешно помещается.")
    void testPlaceParcelValid() {
        List<Dimension> dimensions = List.of(new Dimension(6, 6));
        List<char[][]> emptyTrucks = truckFactoryService.createEmptyTruck(dimensions);
        char[][] truck = emptyTrucks.get(0);
        char[][] pack = {
                {'2', '2'}
        };
        assertThat(parcelLoaderService.placeParcels(truck, pack, 6, 6)).isTrue();
        assertThat('2').isEqualTo(truck[5][0]);
        assertThat('2').isEqualTo(truck[5][1]);
    }

    @Test
    @DisplayName("Проверка, что нельзя поместить вторую посылку на уже занятую область")
    void testPlaceParcelInvalid() {
        List<Dimension> dimensions = List.of(new Dimension(6, 6));
        List<char[][]> emptyTrucks = truckFactoryService.createEmptyTruck(dimensions);
        char[][] truck = emptyTrucks.get(0);
        char[][] pack1 = {
                {3, 3, 3}
        };
        char[][] pack2 = {
                {4, 4, 4}
        };
        assertThat(parcelLoaderService.placeParcels(truck, pack1, 6, 6)).isTrue();
        assertThat(parcelLoaderService.placeParcels(truck, pack2, 6, 6)).isTrue();
    }

    @Test
    @DisplayName("Проверка выброса исключения при недостаточном количестве грузовиков")
    void testPlaceParcelException() {
        List<Dimension> dimensions = List.of(new Dimension(2, 2));
        truckLoadService = new OptimalTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, parcelMapper);
        List<Parcel> parcels = List.of(
                new Parcel(new int[][]{{1, 1}, {1, 1}}),
                new Parcel(new int[][]{{2, 2, 2}, {2, 2, 2}}),
                new Parcel(new int[][]{{3, 3, 3}, {3, 3, 3}})
        );
        assertThatThrownBy(() -> truckLoadService.loadParcels(parcels, dimensions)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("Проверка процесса упаковки по именам нескольких посылок в кузовы.")
    void testLoadParcelsByName() {
        List<Dimension> dimensions = List.of(new Dimension(6, 6));
        truckLoadService = new OptimalTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, parcelMapper);
        String names = "Кофемашина, Чайник";
        List<char[][]> parcelsByName = truckLoadService.loadParcelsByName(names, dimensions).stream().map(Truck::getParcels).toList();
        assertThat(parcelsByName.get(0)[5][0]).isEqualTo('^');
        assertThat(parcelsByName.get(0)[3][0]).isEqualTo('%');
    }
}