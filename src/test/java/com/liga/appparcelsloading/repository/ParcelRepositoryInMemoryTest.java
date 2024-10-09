package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.parcel.repository.ParcelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static com.liga.appparcelsloading.DataTest.GET_BY_NAME_PARCEL;
import static com.liga.appparcelsloading.DataTest.PARCEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class ParcelRepositoryInMemoryTest {

    @Autowired
    private ParcelRepository repository;

    @Test
    @DisplayName("Сохранение новой посылки")
    void save() {
        repository.save(PARCEL);
        assertThat(repository.findAll().size()).isEqualTo(11);
        assertThat(repository.findByName("Стиральная машина").get()).isEqualTo(PARCEL);
        repository.deleteByName(PARCEL.getName());
    }

    @Test
    @DisplayName("Получение посылки по названию")
    void findByName() {
        assertThat(repository.findByName("Чайник").get()).isEqualTo(GET_BY_NAME_PARCEL);
    }

    @Test
    @DisplayName("Получение всех посылок")
    void findAll() {
        assertThat(repository.findAll().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("Удаление посылки по названию")
    @Transactional
    void deleteByName() {
        assertThat(repository.deleteByName("Чайник")).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(9);
        repository.save(GET_BY_NAME_PARCEL);
    }

    @Test
    @DisplayName("Удаление посылки с несуществующим названием")
    void deleteByNameNotFoundName() {
        String parcelName = "Лопата";
        assertThatThrownBy(() -> repository.deleteByName(parcelName))
                .isInstanceOf(org.springframework.dao.InvalidDataAccessApiUsageException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Посылка с названием 'Лопата' не найдена");    }
}