package com.liga.loadingParcelsApp.service;

import com.liga.loadingParcelsApp.model.Package;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.liga.loadingParcelsApp.util.DataTest.TRUCK_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование класса LoadingTrucks")
class LoadingTrucksTest {

    private LoadingTrucks loadingTrucks;

    @BeforeEach
    void setUp() {
        loadingTrucks = new LoadingTrucks();
    }

    @Test
    @DisplayName("Проверка, что метод создает пустой кузов правильного размера, заполненный пробелами")
    void testCreateEmptyTruck() {
        char[][] emptyTruck = loadingTrucks.createEmptyTruck();
        assertThat(TRUCK_SIZE).isEqualTo(emptyTruck.length);
        assertThat(TRUCK_SIZE).isEqualTo(emptyTruck[0].length);
        for (char[] row : emptyTruck) {
            for (char cell : row) {
                assertThat(' ').isEqualTo(cell);
            }
        }
    }

    @Test
    @DisplayName("Проверка, что можно поместить посылку на пустое место")
    void testCanPlaceValid() {
        char[][] truck = loadingTrucks.createEmptyTruck();
        int[][] pack = {
                {1, 1},
                {1, 1}
        };
        assertThat(loadingTrucks.canPlace(truck, pack, 0, 0)).isTrue();
    }

    @Test
    @DisplayName("Проверка, что нельзя поместить посылку на уже занятое место.")
    void testCanPlaceInvalid() {
        char[][] truck = loadingTrucks.createEmptyTruck();
        int[][] pack = {
                {1, 1},
                {1, 1}
        };
        loadingTrucks.applyPackage(truck, pack, 0, 0);
        assertThat(loadingTrucks.canPlace(truck, pack, 0, 0)).isFalse();
    }

    @Test
    @DisplayName("Проверка правильность размещения посылки в кузове")
    void testApplyPackage() {
        char[][] truck = loadingTrucks.createEmptyTruck();
        int[][] pack = {
                {1, 1},
                {1, 1}
        };
        loadingTrucks.applyPackage(truck, pack, 0, 0);
        assertThat('1').isEqualTo(truck[0][0]);
        assertThat('1').isEqualTo(truck[0][1]);
        assertThat('1').isEqualTo(truck[1][0]);
        assertThat('1').isEqualTo(truck[1][1]);
    }

    @Test
    @DisplayName("Проверка, что посылка успешно помещается.")
    void testPlacePackageValid() {
        char[][] truck = loadingTrucks.createEmptyTruck();
        int[][] pack = {
                {2, 2}
        };
        loadingTrucks.placePackage(truck, pack);
        assertThat(loadingTrucks.placePackage(truck, pack)).isTrue();
        assertThat('2').isEqualTo(truck[5][0]);
        assertThat('2').isEqualTo(truck[5][1]);
    }

    @Test
    @DisplayName("Проверка, что нельзя поместить вторую посылку на уже занятую область")
    void testPlacePackageInvalid() {
        char[][] truck = loadingTrucks.createEmptyTruck();
        int[][] pack1 = {
                {3, 3, 3}
        };
        int[][] pack2 = {
                {4, 4, 4}
        };
        loadingTrucks.placePackage(truck, pack1);
        assertThat(loadingTrucks.placePackage(truck, pack2)).isTrue();
    }

    @Test
    @DisplayName("Проверка общего процесса упаковки нескольких посылок в кузовы.")
    void testPackPackages() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{
                {1, 1},
                {1, 1}
        }));
        packages.add(new Package(new int[][]{
                {2, 2, 2}
        }));
        List<char[][]> trucks = loadingTrucks.packPackages(packages, 2);
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