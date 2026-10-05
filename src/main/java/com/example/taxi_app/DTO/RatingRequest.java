package com.example.taxi_app.DTO;

import com.example.taxi_app.Entitys.Enums.RaterRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {

    @NotNull(message = "ID поездки обязателен")
    private Long rideId;

    @NotNull(message = "ID оценивающего обязателен")
    private Long raterId;

    @NotNull(message = "Роль оценивающего обязательна")
    private RaterRole raterRole;

    @Min(value = 1, message = "Оценка не может быть меньше 1")
    @Max(value = 5, message = "Оценка не может быть больше 5")
    private Integer rating; // nullable

    private String comment; // nullable
}