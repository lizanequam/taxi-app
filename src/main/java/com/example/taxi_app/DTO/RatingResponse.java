package com.example.taxi_app.DTO;

import com.example.taxi_app.Entitys.Enums.RaterRole;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    private Long id;
    private Long rideId;
    private Long raterId;
    private RaterRole raterRole;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}