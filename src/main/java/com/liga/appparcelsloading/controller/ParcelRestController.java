package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.dto.ParcelDto;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelRestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления посылками.
 * Предоставляет CRUD-операции для работы с посылками.
 * <p>Контроллер использует {@link ParcelRestService} для выполнения бизнес-логики.</p>
 */
@RestController
@RequestMapping("/parcels")
@AllArgsConstructor
@Slf4j
public class ParcelRestController {

    private final ParcelRestService service;

    /**
     * Получить список всех посылок.
     *
     * @return {@link ResponseEntity} содержащий список всех посылок и статус HTTP.
     */
    @GetMapping
    public ResponseEntity<List<ParcelDto>> getAllParcels() {
        return service.findAll();
    }

    /**
     * Получить информацию о посылке по её идентификатору.
     *
     * @param id Идентификатор посылки.
     * @return {@link ResponseEntity} содержащий информацию о посылке и статус HTTP.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParcelDto> getParcelById(@PathVariable int id) {
        return service.findById(id);
    }

    /**
     * Создать новую посылку.
     *
     * @param parcel Объект {@link Parcel}, представляющий новую посылку.
     * @return {@link ResponseEntity} содержащий созданную посылку и статус HTTP.
     */
    @PostMapping
    public ResponseEntity<Parcel> createParcel(@RequestBody Parcel parcel) {
        return service.create(parcel);
    }

    /**
     * Обновить существующую посылку.
     *
     * @param id     Идентификатор посылки для обновления.
     * @param parcel Объект {@link Parcel} с обновлёнными данными.
     * @return {@link ResponseEntity} содержащий обновлённую посылку и статус HTTP.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Parcel> updateParcel(@PathVariable int id, @RequestBody Parcel parcel) {
        return service.update(id, parcel);
    }

    /**
     * Удалить посылку по её идентификатору.
     *
     * @param id Идентификатор посылки.
     * @return {@link ResponseEntity} с HTTP статусом после удаления.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParcel(@PathVariable int id) {
        return service.deleteById(id);
    }
}
