package com.liga.loading_parcels_app.util;

import com.liga.loading_parcels_app.service.LoadingTrucks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.liga.loading_parcels_app.util.DataTest.TRUCK_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование класса LoadingTrucks")
class LoadingTrucksTest {

    @Test
    @DisplayName("Проверка, что метод создает пустой кузов правильного размера, заполненный пробелами")
    void testCreateEmptyTruck() {
        char[][] emptyTruck = LoadingTrucks.createEmptyTruck();
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
        char[][] truck = LoadingTrucks.createEmptyTruck();
        int[][] pack = {
                {1, 1},
                {1, 1}
        };
        assertThat(LoadingTrucks.canPlace(truck, pack, 0, 0)).isTrue();
    }

    @Test
    @DisplayName("Проверка, что нельзя поместить посылку на уже занятое место.")
    void testCanPlaceInvalid() {
        char[][] truck = LoadingTrucks.createEmptyTruck();
        int[][] pack = {
                {1, 1},
                {1, 1}
        };
        LoadingTrucks.applyPackage(truck, pack, 0, 0);
        assertThat(LoadingTrucks.canPlace(truck, pack, 0, 0)).isFalse();
    }

    @Test
    @DisplayName("Проверка правильность размещения посылки в кузове")
    void testApplyPackage() {
        char[][] truck = LoadingTrucks.createEmptyTruck();
        int[][] pack = {
                {1, 1},
                {1, 1}
        };
        LoadingTrucks.applyPackage(truck, pack, 0, 0);
        assertThat('1').isEqualTo(truck[0][0]);
        assertThat('1').isEqualTo(truck[0][1]);
        assertThat('1').isEqualTo(truck[1][0]);
        assertThat('1').isEqualTo(truck[1][1]);
    }

    @Test
    @DisplayName("Проверка, что посылка успешно помещается.")
    void testPlacePackageValid() {
        char[][] truck = LoadingTrucks.createEmptyTruck();
        int[][] pack = {
                {2, 2}
        };
        LoadingTrucks.placePackage(truck, pack);
        assertThat(LoadingTrucks.placePackage(truck, pack)).isTrue();
        assertThat('2').isEqualTo(truck[5][0]);
        assertThat('2').isEqualTo(truck[5][1]);
    }

    @Test
    @DisplayName("Проверка, что нельзя поместить вторую посылку на уже занятую область")
    void testPlacePackageInvalid() {
        char[][] truck = LoadingTrucks.createEmptyTruck();
        int[][] pack1 = {
                {3, 3, 3}
        };
        int[][] pack2 = {
                {4, 4, 4}
        };
        LoadingTrucks.placePackage(truck, pack1);
        assertThat(LoadingTrucks.placePackage(truck, pack2)).isTrue();
    }

    @Test
    @DisplayName("Проверка общего процесса упаковки нескольких посылок в кузовы.")
    void testPackPackages() {
        List<int[][]> packages = new ArrayList<>();
        packages.add(new int[][]{
                {1, 1},
                {1, 1}
        });
        packages.add(new int[][]{
                {2, 2, 2}
        });
        List<char[][]> trucks = LoadingTrucks.packPackages(packages);
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