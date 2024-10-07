package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.dto.ParcelDto;
import com.liga.appparcelsloading.model.Parcel;
import com.liga.appparcelsloading.service.ParcelRestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parcels")
@AllArgsConstructor
@Slf4j
public class ParcelRestController {

    private final ParcelRestService service;

    @GetMapping
    public ResponseEntity<List<ParcelDto>> getAllParcels() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcelDto> getParcelById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Parcel> createParcel(@RequestBody Parcel parcel) {
        return service.create(parcel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parcel> updateParcel(@PathVariable int id, @RequestBody Parcel parcel) {
        return service.update(id, parcel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParcel(@PathVariable int id) {
        return service.deleteById(id);
    }

}
