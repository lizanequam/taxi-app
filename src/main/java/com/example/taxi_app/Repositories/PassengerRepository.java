package com.example.taxi_app.Repositories;

import com.example.taxi_app.Entitys.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    List<Passenger> findByIsDeletedFalse();

    Optional<Passenger> findByIdAndIsDeletedFalse(Long id);

    // проверка почты (кроме текущего пассажира)
    boolean existsByEmailAndIdNot(String email, Long id);

    // то же ток для телефона
    boolean existsByPhoneAndIdNot(String phone, Long id);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}