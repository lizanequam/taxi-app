package com.example.taxi_app.Entitys;

import com.example.taxi_app.Entitys.Enums.RaterRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ride_id", nullable = false)
    private Long rideId;

    @Column(name = "rater_id", nullable = false)
    private Long raterId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // Защита от ошибки приведения типов в PostgreSQL
    @Column(name = "rater_role", nullable = false, columnDefinition = "rater_role")
    private RaterRole raterRole;

    @Column(name = "rating")
    private Integer rating; // Может быть null

    @Column(name = "comment")
    private String comment; // Может быть null

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}