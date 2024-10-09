package com.liga.appparcelsloading.parcel.repository;

import com.liga.appparcelsloading.parcel.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Репозиторий для управления сущностями {@link Parcel} с использованием JPA.
 *
 * Интерфейс {@code ParcelDataJpaRepository} расширяет {@link JpaRepository} и предоставляет
 * методы для выполнения операций с сущностями {@code Parcel} в базе данных.
 *
 * Аннотация {@link Modifying} указывает, что метод будет изменять данные в базе данных.
 * Аннотация {@link Query} позволяет указать произвольный JPQL-запрос для выполнения.
 */
public interface ParcelDataJpaRepository extends JpaRepository<Parcel, Integer> {
    /**
     * Удаляет сущность {@code Parcel} из базы данных по указанному идентификатору.
     *
     * @param parcelId идентификатор посылки, которую необходимо удалить
     * @return количество удаленных записей (0 или 1)
     */
    @Modifying
    @Query("DELETE FROM Parcel p WHERE p.parcelId = :parcelId")
    int deleteByParcelId(int parcelId);
}
