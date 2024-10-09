package com.liga.appparcelsloading.service.parcel;

import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelRepository;
import com.liga.appparcelsloading.util.ParcelDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления посылками.
 * Предоставляет методы для создания, обновления, получения и удаления посылок.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ParcelInMemoryService {
    private final ParcelRepository repository;
    private final ParcelDataMapper parcelDataMapper;

    /**
     * Сохраняет посылку. Если посылка с заданным именем уже существует, она обновляется.
     *
     * @param name        имя посылки
     * @param symbol      символ посылки
     * @param weight      вес посылки
     * @param orientation ориентация посылки (1 - вертикальная, 2 - горизонтальная)
     * @return сохранённая или обновлённая посылка
     */
    public Parcel save(String name, char symbol, int weight, String orientation) {
        Optional<Parcel> existingParcel = getParcel(name);
        Parcel parcel = existingParcel.orElse(new Parcel());
        saveParcel(parcel, name, symbol, weight, orientation);
        if (existingParcel.isPresent()) {
            log.info("Посылка '{}' успешно обновлена", parcel.getName());
        } else {
            log.info("Посылка '{}' успешно создана", parcel.getName());
        }
        return parcel;
    }

    /**
     * Получает посылку по имени.
     *
     * @param name имя посылки
     * @return найденная посылка, если она существует
     */
    public Optional<Parcel> getParcelByName(String name) {
        return getParcel(name);
    }

    /**
     * Получает список всех посылок.
     *
     * @return список посылок
     */
    public List<Parcel> getAllParcels() {
        log.info("Получение всех посылок");
        return repository.findAll();
    }

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки
     * @return true, если посылка была успешно удалена, иначе false
     */
    public boolean deleteParcel(String name) {
        boolean isDeleted = repository.deleteByName(name);
        if (isDeleted) {
            log.info("Посылка '{}' успешно удалена", name);
        } else {
            log.warn("Не удалось удалить посылку '{}'", name);
        }
        return isDeleted;
    }

    /**
     * Получает посылку по имени.
     *
     * @param nameParcel имя посылки
     * @return опциональная посылка, если она существует
     */
    private Optional<Parcel> getParcel(String nameParcel) {
        return repository.findByName(nameParcel);
    }

    /**
     * Сохраняет или обновляет данные посылки.
     *
     * @param parcel     посылка для сохранения
     * @param name       имя посылки
     * @param symbol     символ посылки
     * @param weight     вес посылки
     * @param orientation ориентация посылки
     */
    private void saveParcel(Parcel parcel, String name, char symbol, int weight, String orientation) {
        parcel.setName(name);
        parcel.setSymbol(symbol);
        int[][] form;
        switch (orientation) {
            case "1": {
                form = parcelDataMapper.setVerticalForm(weight);
                log.debug("Установлена вертикальная форма для посылки '{}'", name);
                break;
            }
            case "2": {
                form = parcelDataMapper.setHorizontalForm(weight);
                log.debug("Установлена горизонтальная форма для посылки '{}'", name);
                break;
            }
            default: {
                form = parcelDataMapper.setHorizontalForm(weight);
                log.warn("Неизвестная ориентация для посылки '{}'. Установлена горизонтальная форма по умолчанию.", name);
            }
        }
        parcel.setForm(form);
        repository.save(parcel);
        log.info("Посылка '{}' сохранена в репозитории", name);
    }
}
