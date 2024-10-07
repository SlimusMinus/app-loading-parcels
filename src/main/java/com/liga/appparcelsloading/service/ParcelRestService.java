package com.liga.appparcelsloading.service;

import com.liga.appparcelsloading.dto.ParcelDto;
import com.liga.appparcelsloading.mapper.ParcelMapper;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.repository.ParcelDataJpaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с посылками через REST API.
 * Предоставляет методы для создания, обновления, получения и удаления посылок.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ParcelRestService {
    private final ParcelDataJpaRepository crudRepository;

    /**
     * Находит посылку по её идентификатору.
     *
     * @param id идентификатор посылки
     * @return ResponseEntity с найденной посылкой или статус 404, если посылка не найдена
     */
    public ResponseEntity<ParcelDto> findById(int id) {
        Optional<Parcel> parcel = crudRepository.findById(id);
        if (parcel.isPresent()) {
            log.info("Найдена посылка с id '{}': {}", id, parcel.get());
            return ResponseEntity.ok(ParcelMapper.INSTANCE.getParcelDto(parcel.get()));
        } else {
            log.warn("Посылка с '{}' id не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Находит все посылки.
     *
     * @return ResponseEntity со списком посылок или статус 204, если посылки отсутствуют
     */
    public ResponseEntity<List<ParcelDto>> findAll() {
        List<ParcelDto> parcels = crudRepository.findAll().stream().map(ParcelMapper.INSTANCE::getParcelDto).toList();
        if (parcels.isEmpty()) {
            log.info("Список посылок пуст");
            return ResponseEntity.noContent().build();
        }
        log.info("Найдено {} посылок", parcels.size());
        return ResponseEntity.ok(parcels);
    }

    /**
     * Создает новую посылку.
     *
     * @param parcel посылка для создания
     * @return ResponseEntity с сохраненной посылкой
     */
    public ResponseEntity<Parcel> create(Parcel parcel) {
        Parcel savedParcel = crudRepository.save(parcel);
        log.info("Посылка сохранена: {}", savedParcel);
        return ResponseEntity.ok(savedParcel);
    }

    /**
     * Обновляет существующую посылку.
     *
     * @param parcelId идентификатор посылки для обновления
     * @param parcel   новые данные посылки
     * @return ResponseEntity с обновленной посылкой или статус 404, если посылка не найдена
     */
    public ResponseEntity<Parcel> update(int parcelId, Parcel parcel) {
        if (!crudRepository.existsById(parcelId)) {
            log.warn("Посылка с ID {} не найдена для обновления", parcelId);
            return ResponseEntity.notFound().build();
        }
        parcel.setParcelId(parcelId);
        Parcel updatedParcel = crudRepository.save(parcel);
        log.info("Посылка обновлена: {}", updatedParcel);
        return ResponseEntity.ok(updatedParcel);
    }

    /**
     * Удаляет посылку по её идентификатору.
     *
     * @param parcelId идентификатор посылки для удаления
     * @return ResponseEntity с пустым телом и статусом 204, если посылка удалена,
     *         или статус 404, если посылка не найдена
     */
    @Transactional
    public ResponseEntity<Void> deleteById(int parcelId) {
        int isDeleted = crudRepository.deleteByParcelId(parcelId);
        if (isDeleted != 0) {
            log.info("Посылка с ID {} удалена", parcelId);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Посылка с ID {} не найдена", parcelId);
            return ResponseEntity.notFound().build();
        }
    }
}
