package org.yalli.wah.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank
    private String fullName;
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String country;
    @NotNull
    private LocalDate birthDate;
}
