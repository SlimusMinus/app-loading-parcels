package com.liga.appparcelsloading.parcel.repository;

import com.liga.appparcelsloading.parcel.model.Parcel;
import com.liga.appparcelsloading.util.ParcelDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация интерфейса ParcelRepository,
 * отвечающая за управление объектами Parcel в памяти.
 */
@Slf4j
@Repository
public class ParcelRepositoryInMemory implements ParcelRepository {
    private final ParcelDataMapper parcelDataMapper;

    private final Map<String, Parcel> parcels = new ConcurrentHashMap<>();

    public ParcelRepositoryInMemory(ParcelDataMapper parcelDataMapper) {
        this.parcelDataMapper = parcelDataMapper;
    }

    @PostConstruct
    public void init() {
        this.parcels.putAll(parcelDataMapper.getAllParcels());
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
    public Optional<Parcel> findByName(String name) {
        Optional<Parcel> parcelOpt = Optional.ofNullable(parcels.get(name));
        parcelOpt.ifPresentOrElse(
                parcel -> log.info("Получена посылка с названием '{}'", name),
                () -> log.warn("Посылка с названием '{}' не найдена", name)
        );
        return parcelOpt;
    }

    /**
     * Получает все посылки из репозитория.
     *
     * @return список всех посылок.
     */
    @Override
    public List<Parcel> findAll() {
        log.info("Получение всех посылок, количество: {}", parcels.size());
        return parcels.values().stream().toList();
    }

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки, которую нужно удалить.
     * @return true, если посылка была успешно удалена, или false, если посылка не найдена.
     */
    @Override
    public boolean deleteByName(String name) {
        Parcel removedParcel = parcels.remove(name);
        if (removedParcel == null) {
            log.warn("Попытка удаления несуществующей посылки с названием '{}'", name);
            throw new IllegalArgumentException("Посылка с названием '" + name + "' не найдена");
        }
        log.info("Удалена посылка с названием '{}'", name);
        return true;
    }

}
