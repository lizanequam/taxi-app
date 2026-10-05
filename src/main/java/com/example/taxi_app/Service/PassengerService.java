package com.example.taxi_app.Service;

import com.example.taxi_app.DTO.PassengerRequest;
import com.example.taxi_app.DTO.PassengerResponse;
import com.example.taxi_app.Entitys.Passenger;
import com.example.taxi_app.Repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Transactional
    public PassengerResponse createPassenger(PassengerRequest request) {
        if (passengerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пассажир с таким email уже существует");
        }
        if (passengerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Пассажир с таким телефоном уже существует");
        }
        Passenger passenger = Passenger.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .isDeleted(false)
                .build();

        Passenger savedPassenger = passengerRepository.save(passenger);
        return mapToResponse(savedPassenger);
    }

    @Transactional(readOnly = true)
    public List<PassengerResponse> getAllPassengers() {
        return passengerRepository.findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PassengerResponse getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Пассажир с ID " + id + " не найден"));
        return mapToResponse(passenger);
    }


    @Transactional
    public PassengerResponse updatePassenger(Long id, PassengerRequest request) {
        Passenger passenger = passengerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Пассажир с ID " + id + " не найден"));

        // Проверка уникальности email и телефона (исключая текущего пассажира)
        if (passengerRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Пассажир с таким email уже существует");
        }
        if (passengerRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new IllegalArgumentException("Пассажир с таким телефоном уже существует");
        }

        passenger.setName(request.getName());
        passenger.setEmail(request.getEmail());
        passenger.setPhone(request.getPhone());

        Passenger updatedPassenger = passengerRepository.save(passenger);
        return mapToResponse(updatedPassenger);
    }

    @Transactional
    public void deletePassenger(Long id) {
        Passenger passenger = passengerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Пассажир с ID " + id + " не найден"));

        passenger.setIsDeleted(true);
        passengerRepository.save(passenger);
    }


    private PassengerResponse mapToResponse(Passenger passenger) {
        return PassengerResponse.builder()
                .id(passenger.getId())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .phone(passenger.getPhone())
                .createdAt(passenger.getCreatedAt())
                .build();
    }
}
