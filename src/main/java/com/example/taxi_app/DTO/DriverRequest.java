package com.example.taxi_app.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class DriverRequest {
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank @Email(message = "Некорректный email")
    private String email;

    @NotBlank @Pattern(regexp = "^\\+375\\d{9}$", message = "Формат: +375291234567")
    private String phone;
}