package com.example.taxi_app.Repositories;

import com.example.taxi_app.Entitys.Rating;
import com.example.taxi_app.Entitys.Enums.RaterRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRideId(Long rideId);
    Optional<Rating> findByRideIdAndRaterIdAndRaterRole(Long rideId, Long raterId, RaterRole raterRole);
}
