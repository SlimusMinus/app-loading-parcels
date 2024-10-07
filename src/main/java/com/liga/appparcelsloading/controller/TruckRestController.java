package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.dto.TruckDto;
import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.TruckRestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trucks")
@AllArgsConstructor
public class TruckRestController {
    private final TruckRestService truckRestService;

    @PostMapping("/load")
    public ResponseEntity<List<Truck>> loadTrucks(
            @RequestParam String algorithmType,
            @RequestParam String heights,
            @RequestParam String weights) {
        Optional<List<Truck>> loadedTrucks = truckRestService.load(algorithmType, heights, weights);
        return loadedTrucks
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/load-by-name")
    public ResponseEntity<List<Truck>> loadTrucksByName(
            @RequestParam String algorithmType,
            @RequestParam String nameParcels,
            @RequestParam String heights,
            @RequestParam String weights) {
        Optional<List<Truck>> loadedTrucks = truckRestService.loadByName(algorithmType, nameParcels, heights, weights);
        return loadedTrucks
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @GetMapping
    public ResponseEntity<List<TruckDto>> getTrucks() {
        return truckRestService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TruckDto> getTruckById(@PathVariable int id){
        return truckRestService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruckById(@PathVariable int id) {
        return truckRestService.deleteById(id);
    }
}
