package com.example.taxi_app.Service;

import com.example.taxi_app.DTO.RideRequest;
import com.example.taxi_app.DTO.RideResponse;
import com.example.taxi_app.Entitys.Ride;
import com.example.taxi_app.Entitys.Enums.RideStatus;
import com.example.taxi_app.Repositories.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;

    // допустимые переходы статусов
    private static final Map<RideStatus, Set<RideStatus>> VALID_TRANSITIONS = Map.of(
            RideStatus.CREATED, Set.of(RideStatus.ACCEPTED, RideStatus.CANCELED),
            RideStatus.ACCEPTED, Set.of(RideStatus.EN_ROUTE_TO_PASSENGER, RideStatus.CANCELED),
            RideStatus.EN_ROUTE_TO_PASSENGER, Set.of(RideStatus.EN_ROUTE_TO_DESTINATION, RideStatus.CANCELED),
            RideStatus.EN_ROUTE_TO_DESTINATION, Set.of(RideStatus.COMPLETED),
            RideStatus.COMPLETED, Set.of(), // конец поездочки
            RideStatus.CANCELED, Set.of()   // тож конец, но можно с любого статуса перейти в отмененный
    );

    @Transactional
    public RideResponse createRide(RideRequest request) {
        Ride ride = Ride.builder()
                .driverId(request.getDriverId())
                .passengerId(request.getPassengerId())
                .pickupAddress(request.getPickupAddress())
                .destinationAddress(request.getDestinationAddress())
                .orderedAt(request.getOrderedAt())
                .cost(request.getCost())
                .status(RideStatus.CREATED)
                .build();

        return mapToResponse(rideRepository.save(ride));
    }

    @Transactional(readOnly = true)
    public List<RideResponse> getAllRides() {
        return rideRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RideResponse getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Поездка с ID " + id + " не найдена"));
        return mapToResponse(ride);
    }

    @Transactional
    public RideResponse updateRide(Long id, RideRequest request) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Поездка не найдена"));

        // обновление всех данных кроме статуса, он обновляется в методе ниже
        ride.setDriverId(request.getDriverId());
        ride.setPassengerId(request.getPassengerId());
        ride.setPickupAddress(request.getPickupAddress());
        ride.setDestinationAddress(request.getDestinationAddress());
        ride.setOrderedAt(request.getOrderedAt());
        ride.setCost(request.getCost());

        return mapToResponse(rideRepository.save(ride));
    }

    @Transactional
    public RideResponse updateRideStatus(Long id, RideStatus newStatus) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Поездка не найдена"));

        RideStatus currentStatus = ride.getStatus();

        if (!VALID_TRANSITIONS.getOrDefault(currentStatus, Set.of()).contains(newStatus)) {
            throw new IllegalArgumentException(
                    String.format("Недопустимый переход статуса: из %s в %s", currentStatus, newStatus)
            );
        }

        ride.setStatus(newStatus);
        return mapToResponse(rideRepository.save(ride));
    }

    @Transactional
    public void deleteRide(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Поездка не найдена"));
        rideRepository.delete(ride);
    }

    private RideResponse mapToResponse(Ride ride) {
        return RideResponse.builder()
                .id(ride.getId())
                .driverId(ride.getDriverId())
                .passengerId(ride.getPassengerId())
                .pickupAddress(ride.getPickupAddress())
                .destinationAddress(ride.getDestinationAddress())
                .status(ride.getStatus())
                .orderedAt(ride.getOrderedAt())
                .cost(ride.getCost())
                .createdAt(ride.getCreatedAt())
                .build();
    }
}