package com.liga.appparcelsloading.parcel.controller;

import com.liga.appparcelsloading.parcel.dto.ParcelDto;
import com.liga.appparcelsloading.parcel.model.Parcel;
import com.liga.appparcelsloading.parcel.service.ParcelRestService;
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
     * @param parcelDto Объект {@link Parcel}, представляющий новую посылку.
     * @return {@link ResponseEntity} содержащий созданную посылку и статус HTTP.
     */
    @PostMapping
    public ResponseEntity<ParcelDto> createParcel(@RequestBody ParcelDto parcelDto) {
        return service.create(parcelDto);
    }

    /**
     * Обновить существующую посылку.
     *
     * @param id     Идентификатор посылки для обновления.
     * @param parcelDto Объект {@link Parcel} с обновлёнными данными.
     * @return {@link ResponseEntity} содержащий обновлённую посылку и статус HTTP.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParcelDto> updateParcel(@PathVariable int id, @RequestBody ParcelDto parcelDto) {
        return service.update(id, parcelDto);
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
