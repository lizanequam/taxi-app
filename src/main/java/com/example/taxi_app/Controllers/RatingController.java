package com.example.taxi_app.Controllers;

import com.example.taxi_app.DTO.RatingRequest;
import com.example.taxi_app.DTO.RatingResponse;
import com.example.taxi_app.Service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // создание или обновление оценки
    @PostMapping
    public ResponseEntity<RatingResponse> createOrUpdateRating(@Valid @RequestBody RatingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.createOrUpdateRating(request));
    }

    // оценки конкретной поездки
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByRideId(@PathVariable Long rideId) {
        return ResponseEntity.ok(ratingService.getRatingsByRideId(rideId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getRatingById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }
}
