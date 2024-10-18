package org.yalli.wah.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.SocialMedia;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank
    private String fullName;
    @NotBlank(message = "EMAIL_REQUIRED")
    @Email
    private String email;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Pattern(regexp = "^(?=.*[0-9!@#$%^&*])(?=.*[A-Z]).{8,20}$",
            message = "INVALID_PASSWORD_FORMAT")
    private String password;
    @NotBlank
    private String country;
    private LocalDate birthDate;
    private Map<SocialMedia,String>socialMediaLinks;
}
