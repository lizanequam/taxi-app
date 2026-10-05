package com.example.taxi_app.Service;

import com.example.taxi_app.DTO.*;
import com.example.taxi_app.Entitys.Car;
import com.example.taxi_app.Entitys.Driver;
import com.example.taxi_app.Repositories.CarRepository;
import com.example.taxi_app.Repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;

    @Transactional
    public DriverResponse createDriver(DriverRequest request) {
        if (driverRepository.existsByEmail(request.getEmail())) throw new IllegalArgumentException("Email уже занят");
        if (driverRepository.existsByPhone(request.getPhone())) throw new IllegalArgumentException("Телефон уже занят");

        Driver driver = Driver.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        return mapToResponse(driverRepository.save(driver));
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findByIsDeletedFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Водитель не найден"));
        return mapToResponse(driver);
    }

    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Водитель не найден"));

        if (driverRepository.existsByEmailAndIdNot(request.getEmail(), id)) throw new IllegalArgumentException("Email уже занят");
        if (driverRepository.existsByPhoneAndIdNot(request.getPhone(), id)) throw new IllegalArgumentException("Телефон уже занят");

        driver.setName(request.getName());
        driver.setEmail(request.getEmail());
        driver.setPhone(request.getPhone());

        return mapToResponse(driverRepository.save(driver));
    }

    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = driverRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Водитель не найден"));

        driver.setIsDeleted(true);
        // При soft delete водителя, мягко удаляем и его машины
        driver.getCars().forEach(car -> car.setIsDeleted(true));
        driverRepository.save(driver);
    }

    private DriverResponse mapToResponse(Driver driver) {
        List<CarResponse> cars = driver.getCars().stream()
                .filter(car -> !car.getIsDeleted())
                .map(this::mapCarToResponse)
                .collect(Collectors.toList());

        return DriverResponse.builder()
                .id(driver.getId())
                .name(driver.getName())
                .email(driver.getEmail())
                .phone(driver.getPhone())
                .createdAt(driver.getCreatedAt())
                .cars(cars)
                .build();
    }

    private CarResponse mapCarToResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .driverId(car.getDriver().getId())
                .color(car.getColor())
                .brand(car.getBrand())
                .plate(car.getPlate())
                .createdAt(car.getCreatedAt())
                .build();
    }
}