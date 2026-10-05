package com.example.taxi_app.Service;

import com.example.taxi_app.DTO.CarRequest;
import com.example.taxi_app.DTO.CarResponse;
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
public class CarService {

    private final CarRepository carRepository;
    private final DriverRepository driverRepository;

    @Transactional(readOnly = true)
    public List<CarResponse> getAllCars() {
        return carRepository.findAllByIsDeletedFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public CarResponse getCarById(Long id) {
        Car car = carRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Машина с ID " + id + " не найдена или удалена"));
        return mapToResponse(car);
    }

    @Transactional
    public CarResponse createCar(CarRequest request) {
        if (carRepository.existsByPlate(request.getPlate())) {
            throw new IllegalArgumentException("Номер машины уже зарегистрирован");
        }

        Driver driver = driverRepository.findByIdAndIsDeletedFalse(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Водитель не найден или удален"));

        Car car = Car.builder()
                .driver(driver)
                .color(request.getColor())
                .brand(request.getBrand())
                .plate(request.getPlate())
                .build();

        return mapToResponse(carRepository.save(car));
    }

    @Transactional(readOnly = true)
    public List<CarResponse> getCarsByDriverId(Long driverId) {
        return carRepository.findByDriverIdAndIsDeletedFalse(driverId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = carRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Машина не найдена"));
        car.setIsDeleted(true);
        carRepository.save(car);
    }

    private CarResponse mapToResponse(Car car) {
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