package com.example.taxi_app.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CarRequest {
    @NotNull(message = "ID водителя обязателен")
    private Long driverId;

    @NotBlank(message = "Цвет не может быть пустым")
    private String color;

    @NotBlank(message = "Марка не может быть пустой")
    private String brand;

    @NotBlank
    @Pattern(regexp = "^(\\d{4}[\\s-]?[A-Z]{2}[\\s-]?\\d|E[\\s-]?\\d{3}[\\s-]?[A-Z]{2}[\\s-]?\\d)$",
            message = "Неверный формат номера. Примеры: 1234AB5, E 123 AB 5")
    private String plate;
}