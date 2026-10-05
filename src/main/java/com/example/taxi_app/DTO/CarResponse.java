package com.example.taxi_app.DTO;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CarResponse {
    private Long id;
    private Long driverId;
    private String color;
    private String brand;
    private String plate;
    private LocalDateTime createdAt;
}