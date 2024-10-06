package com.liga.appparcelsloading.controller;

import com.liga.appparcelsloading.model.Truck;
import com.liga.appparcelsloading.service.TruckRestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trucks")
@AllArgsConstructor
public class TruckRestController {
    private final TruckRestService truckRestService;

    @GetMapping("/load")
    public ResponseEntity<List<Truck>> loadTrucks(
            @RequestParam String algorithmType,
            @RequestParam String heights,
            @RequestParam String weights) {

        Optional<List<Truck>> loadedTrucks = truckRestService.load(algorithmType, heights, weights);

        return loadedTrucks
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}
