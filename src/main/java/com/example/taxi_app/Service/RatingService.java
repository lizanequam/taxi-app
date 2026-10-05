package com.example.taxi_app.Service;

import com.example.taxi_app.DTO.RatingRequest;
import com.example.taxi_app.DTO.RatingResponse;
import com.example.taxi_app.Entitys.Rating;
import com.example.taxi_app.Entitys.Ride;
import com.example.taxi_app.Entitys.Enums.RaterRole;
import com.example.taxi_app.Repositories.RatingRepository;
import com.example.taxi_app.Repositories.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RideRepository rideRepository;

    @Transactional
    public RatingResponse createOrUpdateRating(RatingRequest request) {

        Ride ride = rideRepository.findById(request.getRideId())
                .orElseThrow(() -> new RuntimeException("Поездка с ID " + request.getRideId() + " не найдена"));

        // проверка, что raterId участник этой поездки (метод ниже)
        validateRaterParticipation(ride, request.getRaterId(), request.getRaterRole());

        // проверка, не оценил ли он уже эту поездку
        Rating rating = ratingRepository.findByRideIdAndRaterIdAndRaterRole(
                request.getRideId(), request.getRaterId(), request.getRaterRole()
        ).orElse(null);

        if (rating == null) {
            rating = Rating.builder()
                    .rideId(request.getRideId())
                    .raterId(request.getRaterId())
                    .raterRole(request.getRaterRole())
                    .rating(request.getRating())
                    .comment(request.getComment())
                    .build();
        } else {
            // обновляем существующую оценку
            rating.setRating(request.getRating());
            rating.setComment(request.getComment());
        }

        try {
            return mapToResponse(ratingRepository.save(rating));
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Оценка от этого пользователя за данную поездку уже существует");
        }
    }

    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByRideId(Long rideId) {
        return ratingRepository.findByRideId(rideId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RatingResponse getRatingById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Оценка с ID " + id + " не найдена"));
        return mapToResponse(rating);
    }

    // метод для проверки участия в поездке
    private void validateRaterParticipation(Ride ride, Long raterId, RaterRole raterRole) {
        if (raterRole == RaterRole.PASSENGER && !ride.getPassengerId().equals(raterId)) {
            throw new IllegalArgumentException("Пользователь с ID " + raterId + " не является пассажиром в этой поездке");
        }
        if (raterRole == RaterRole.DRIVER && !ride.getDriverId().equals(raterId)) {
            throw new IllegalArgumentException("Пользователь с ID " + raterId + " не является водителем в этой поездке");
        }
    }

    private RatingResponse mapToResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .rideId(rating.getRideId())
                .raterId(rating.getRaterId())
                .raterRole(rating.getRaterRole())
                .rating(rating.getRating())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}