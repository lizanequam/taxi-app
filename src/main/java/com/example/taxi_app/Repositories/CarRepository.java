package com.example.taxi_app.Repositories;

import com.example.taxi_app.Entitys.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findAllByIsDeletedFalse();
    List<Car> findByDriverIdAndIsDeletedFalse(Long driverId);
    Optional<Car> findByIdAndIsDeletedFalse(Long id);
    boolean existsByPlateAndIdNot(String plate, Long id);
    boolean existsByPlate(String plate);
}