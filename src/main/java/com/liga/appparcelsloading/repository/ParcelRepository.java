package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.model.Parcel;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для управления объектами Parcel в репозитории.
 * Предоставляет методы для сохранения, получения, удаления
 * и получения всех посылок.
 */
public interface ParcelRepository {
    /**
     * Сохраняет посылку в репозитории.
     *
     * @param parcel посылка, которую нужно сохранить.
     * @return ранее сохраненная посылка с таким же именем, или null, если это была новая запись.
     */
    Parcel save(Parcel parcel);

    /**
     * Получает посылку по имени.
     *
     * @param name имя посылки, которую нужно получить.
     * @return посылка с указанным именем, или null, если посылка не найдена.
     */
    Optional<Parcel> findByName(String name);

    /**
     * Получает все посылки из репозитория.
     *
     * @return список всех посылок.
     */
    List<Parcel> findAll();

    /**
     * Удаляет посылку по имени.
     *
     * @param parcelName уникальный идентификатор посылки, которую нужно удалить.
     * @return true, если посылка была успешно удалена, или false, если посылка не найдена.
     */
    boolean deleteByName(String parcelName);

    boolean deleteById(int parcelId);
}
