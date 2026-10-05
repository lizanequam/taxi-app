package com.example.taxi_app.Repositories;

import com.example.taxi_app.Entitys.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByIsDeletedFalse();
    Optional<Driver> findByIdAndIsDeletedFalse(Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}