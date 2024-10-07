package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.dto.ParcelDto;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelRestService;
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
import java.util.Objects;

import static com.liga.appparcelsloading.DataTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest
@Slf4j
@Transactional
@SuppressWarnings("resource")
@DisplayName("Тестирование класса ParcelRestService")
public class ParcelJpaStorageTest {

    @Autowired
    private ParcelRestService parcelRestService;

    @Autowired
    private ParcelDataJpaRepository crudRepository;

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
            statement.execute(createTable());
            statement.execute(populateTable());
        } catch (SQLException e) {
            log.error("SQL got exception", e);
        }
    }

    @Test
    @DisplayName("Тестирование метода findById - посылка найдена")
    void testFindById_Found() {
        Parcel parcel = crudRepository.save(PARCEL);
        ResponseEntity<ParcelDto> response = parcelRestService.findById(parcel.getParcelId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Стиральная машина");
    }

    @Test
    @DisplayName("Тестирование метода findById - посылка не найдена")
    void testFindById_NotFound() {
        ResponseEntity<ParcelDto> response = parcelRestService.findById(NOT_VALID_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Тестирование метода findAll - посылки найдены")
    void testFindAll_Found() {
        ResponseEntity<List<ParcelDto>> response = parcelRestService.findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(PARCELS_FROM_FILE);
    }

    @Test
    @DisplayName("Тестирование метода create - создание посылки")
    void testCreate() {
        ResponseEntity<Parcel> newParcel = parcelRestService.create(PARCEL);
        ResponseEntity<List<ParcelDto>> response = parcelRestService.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(SIZE_AFTER_SAVE);
        assertThat(Objects.requireNonNull(newParcel.getBody()).getName()).isEqualTo("Стиральная машина");
    }

    @Test
    @DisplayName("Тестирование метода update - обновление посылки")
    void testUpdate() {
        ResponseEntity<Parcel> response = parcelRestService.update(UPDATE_ID, PARCEL);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Стиральная машина");
    }

    @Test
    @DisplayName("Тестирование метода update - посылка не найдена для обновления")
    void testUpdate_NotFound() {
        Parcel parcel = new Parcel();
        parcel.setName("Не существующая посылка");
        ResponseEntity<Parcel> response = parcelRestService.update(NOT_VALID_ID, parcel);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Тестирование метода deleteById - удаление посылки")
    void testDeleteById() {
        ResponseEntity<Void> response = parcelRestService.deleteById(DELETE_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(crudRepository.findById(7)).isEmpty();
    }

    @Test
    @DisplayName("Тестирование метода deleteById - посылка не найдена для удаления")
    void testDeleteById_NotFound() {
        ResponseEntity<Void> response = parcelRestService.deleteById(NOT_VALID_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String createTable() {
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

    private String populateTable() {
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
