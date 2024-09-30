package com.liga.appparcelsloading.repository;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.util.ParcelMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Реализация интерфейса ParcelRepository,
 * отвечающая за управление объектами Parcel в памяти.
 */
@Slf4j
public class DefaultParcelRepository implements ParcelRepository {
    private final Map<String, Parcel> parcels;

    public DefaultParcelRepository(ParcelMapper parcelMapper) {
        this.parcels = parcelMapper.getAllParcels();
        log.info("Инициализация DefaultParcelRepository с {} посылками", parcels.size());
    }

    /**
     * Сохраняет посылку в репозитории.
     * Если посылка с таким же именем уже существует, она будет обновлена.
     *
     * @param parcel посылка, которую нужно сохранить.
     * @return ранее сохраненная посылка с таким же именем, или null, если это была новая запись.
     */
    @Override
    public Parcel save(Parcel parcel) {
        Parcel saveParcel = parcels.put(parcel.getName(), parcel);
        if (saveParcel == null) {
            log.info("Добавлена новая посылка с названием '{}'", parcel.getName());
        } else {
            log.info("Обновлена посылка с названием '{}'", parcel.getName());
        }
        return saveParcel;
    }

    /**
     * Получает посылку по имени.
     *
     * @param name имя посылки, которую нужно получить.
     * @return посылка с указанным именем, или null, если посылка не найдена.
     */
    @Override
    public Parcel getByName(String name) {
        Parcel parcel = parcels.get(name);
        if (parcel == null) {
            log.warn("Посылка с названием '{}' не найдена", name);
        } else {
            log.info("Получена посылка с названием '{}'", name);
        }
        return parcel;
    }

    /**
     * Получает все посылки из репозитория.
     *
     * @return список всех посылок.
     */
    @Override
    public List<Parcel> getAll() {
        log.info("Получение всех посылок, количество: {}", parcels.size());
        return new ArrayList<>(parcels.values());
    }

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки, которую нужно удалить.
     * @return true, если посылка была успешно удалена, или false, если посылка не найдена.
     */
    @Override
    public boolean delete(String name) {
        if (parcels.remove(name) == null) {
            log.warn("Посылка с названием '{}' не найдена", name);
            return false;
        }
        log.info("Удалена посылка с названием '{}'", name);
        return true;
    }
}
