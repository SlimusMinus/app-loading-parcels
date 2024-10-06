package com.liga.appparcelsloading.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.liga.appparcelsloading.DataTest.GET_BY_NAME_PARCEL;
import static com.liga.appparcelsloading.DataTest.PARCEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class DefaultParcelRepositoryTest {

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
                .hasMessage("Посылка с названием '" + parcelName + "' не найдена");
    }
}