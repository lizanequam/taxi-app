package com.example.taxi_app.Controllers;

import com.example.taxi_app.DTO.RideRequest;
import com.example.taxi_app.DTO.RideResponse;
import com.example.taxi_app.DTO.RideStatusUpdateRequest;
import com.example.taxi_app.Service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody RideRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rideService.createRide(request));
    }

    @GetMapping
    public ResponseEntity<List<RideResponse>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RideResponse> updateRide(@PathVariable Long id, @Valid @RequestBody RideRequest request) {
        return ResponseEntity.ok(rideService.updateRide(id, request));
    }

    // смена статуса
    @PatchMapping("/{id}/status")
    public ResponseEntity<RideResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody RideStatusUpdateRequest request) {
        return ResponseEntity.ok(rideService.updateRideStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
        return ResponseEntity.noContent().build();
    }

}