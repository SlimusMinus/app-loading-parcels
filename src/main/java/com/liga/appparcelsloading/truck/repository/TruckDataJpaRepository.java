package com.liga.appparcelsloading.truck.repository;

import com.liga.appparcelsloading.truck.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Репозиторий для управления сущностями {@link Truck} с использованием JPA.
 *
 * Интерфейс {@code TruckDataJpaRepository} расширяет {@link JpaRepository} и предоставляет
 * методы для выполнения операций с сущностями {@code Truck} в базе данных.
 *
 * Аннотация {@link Modifying} указывает, что метод будет изменять данные в базе данных.
 * Аннотация {@link Query} позволяет указать произвольный JPQL-запрос для выполнения.
 */
public interface TruckDataJpaRepository extends JpaRepository<Truck, Integer> {

    /**
     * Удаляет сущность {@code Truck} из базы данных по указанному идентификатору.
     *
     * @param id идентификатор грузовика, который необходимо удалить
     * @return количество удаленных записей (0 или 1)
     */
    @Modifying
    @Query("DELETE FROM Truck t WHERE t.id = :id")
    int deleteById(int id);
}
