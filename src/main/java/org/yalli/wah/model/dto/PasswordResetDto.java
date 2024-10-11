package org.yalli.wah.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDto {
    private String email;
    @NotBlank(message = "PASSWORD_REQUIRED")
    @Pattern(regexp = "^(?=.*[0-9!@#$%^&*])(?=.*[A-Z]).{8,20}$",
            message = "INVALID_PASSWORD_FORMAT")
    private String newPassword;

}