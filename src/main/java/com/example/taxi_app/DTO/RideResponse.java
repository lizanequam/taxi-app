package com.example.taxi_app.DTO;

import com.example.taxi_app.Entitys.Enums.RideStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {
    private Long id;
    private Long driverId;
    private Long passengerId;
    private String pickupAddress;
    private String destinationAddress;
    private RideStatus status;
    private LocalDateTime orderedAt;
    private BigDecimal cost;
    private LocalDateTime createdAt;
}