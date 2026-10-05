package com.example.taxi_app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {

    @NotNull(message = "ID водителя обязателен")
    private Long driverId;

    @NotNull(message = "ID пассажира обязателен")
    private Long passengerId;

    @NotBlank(message = "Адрес отправления не может быть пустым")
    private String pickupAddress;

    @NotBlank(message = "Адрес назначения не может быть пустым")
    private String destinationAddress;

    @NotNull(message = "Дата и время заказа обязательны")
    private LocalDateTime orderedAt;

    @PositiveOrZero(message = "Стоимость не может быть отрицательной")
    private BigDecimal cost;
}