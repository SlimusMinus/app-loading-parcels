package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.dto.TruckDto;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.TruckRestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest
@Slf4j
@Transactional
@SuppressWarnings("resource")
@DisplayName("Тестирование класса ParcelRestService")
public class TruckJpaStorageTest {

    @Autowired
    private TruckRestService service;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @BeforeEach
    @DisplayName("Создание схемы и таблиц базы данных, а также их заполнение перед тестом")
    void setUp() {
        try (Connection connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
             Statement statement = connection.createStatement()) {
            statement.execute(createTruckTable());
            statement.execute(createParcelTable());
            statement.execute(populateParcelTable());
        } catch (SQLException e) {
            log.error("SQL got exception", e);
        }
    }

    @Test
    @DisplayName("Загрузка посылок с использованием алгоритма")
    void testLoadParcels() {
        Optional<List<Truck>> result = service.load("optimal", "7,7,7", "6,6,6");
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
    }

    @Test
    @DisplayName("Загрузка посылок по имени с использованием алгоритма")
    void testLoadParcelsByName() {
        Optional<List<Truck>> result = service.loadByName("even", "Кофемашина,Холодильник,Пылесос,Наушники,Телевизор", "6,6,6", "7,7,8");
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(3);
    }

    @Test
    @DisplayName("Нахождение всех грузовиков")
    void testFindAllTrucks() {
        service.load("optimal", "7,7,7", "6,6,6");
        service.loadByName("even", "Кофемашина,Холодильник,Пылесос,Наушники,Телевизор", "6,6,6", "7,7,8");

        ResponseEntity<List<TruckDto>> response = service.findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("Нахождение грузовика по id")
    void testFindTruckById() {
        Optional<List<Truck>> optimal = service.load("optimal", "7,7,7", "6,6,6");
        ResponseEntity<TruckDto> response = service.findById(optimal.get().get(0).getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Удаление грузовика по id")
    void testDeleteTruckById() {
        Optional<List<Truck>> optimal = service.load("optimal", "7,7,7", "6,6,6");
        ResponseEntity<Void> response = service.deleteById(optimal.get().get(0).getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    private String createTruckTable() {
        return """
                DROP TABLE IF EXISTS trucks;
                CREATE TABLE trucks
                  (
                      id           SERIAL PRIMARY KEY,
                      name_truck   VARCHAR(255) NOT NULL,
                      name_parcels VARCHAR(255) NOT NULL,
                      parcels      TEXT         NOT NULL
                  );
                """;
    }

    private String createParcelTable() {
        return """
                CREATE TABLE IF NOT EXISTS parcels
                (
                    id     SERIAL PRIMARY KEY,
                    name   VARCHAR(255),
                    symbol CHAR(1),
                    form   TEXT
                );
                """;
    }

    private String populateParcelTable() {
        return """
                INSERT INTO parcels (name, symbol, form)
                VALUES
                    ('Телевизор', '?', '[ [999, 999, 999] ]'),
                    ('Кофемашина', '^', '[ [666, 666] ]'),
                    ('Чайник', '%', '[ [55555] ]'),
                    ('Холодильник', '*', '[ [8888, 8888] ]'),
                    ('Плита', '!', '[ [1] ]'),
                    ('Приставка', '@', '[ [22] ]'),
                    ('Комбайн', '#', '[ [333] ]'),
                    ('Пылесос', '&', '[ [777, 7777] ]'),
                    ('Телефон', '^', '[ [666, 666] ]'),
                    ('Наушники', '$', '[ [4444] ]');
                """;
    }
}
