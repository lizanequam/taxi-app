package com.example.taxi_app.DTO;

import com.example.taxi_app.Entitys.Enums.RideStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideStatusUpdateRequest {
    @NotNull(message = "Новый статус не может быть пустым")
    private RideStatus status;
}