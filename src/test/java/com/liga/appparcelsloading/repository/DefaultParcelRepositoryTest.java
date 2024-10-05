package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.util.ParcelMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.liga.appparcelsloading.DataTest.GET_BY_NAME_PARCEL;
import static com.liga.appparcelsloading.DataTest.PARCEL;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultParcelRepositoryTest {
    private final ParcelMapper parcelMapper = new ParcelMapper();
    private final ParcelRepository repository = new DefaultParcelRepository(parcelMapper);

    @Test
    @DisplayName("Сохранение новой посылки")
    void save() {
        repository.save(PARCEL);
        assertThat(repository.findAll().size()).isEqualTo(11);
        assertThat(repository.findByName("Стиральная машина")).isEqualTo(PARCEL);
    }

    @Test
    @DisplayName("Получение посылки по названию")
    void findByName() {
        assertThat(repository.findByName("Чайник")).isEqualTo(GET_BY_NAME_PARCEL);
    }

    @Test
    @DisplayName("Получение всех посылок")
    void findAll() {
        assertThat(repository.findAll().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("Удаление посылки по названию")
    void delete() {
        assertThat(repository.delete("Чайник")).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(9);
    }

    @Test
    @DisplayName("Удаление посылки с несуществующим названием")
    void deleteNotFoundName() {
        assertThat(repository.delete("Лопата")).isFalse();
    }
}