package org.yalli.wah.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetDto {
    private String email;
    private String otp;
    private String newPassword;
    private String newPasswordRepeat;


}