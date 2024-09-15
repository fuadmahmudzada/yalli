package org.yalli.wah.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {

        private String email;


        private String firstName;

     //   @NotBlank(message = "Last name is required")
        private String lastName;

//@NotBlank(message = "Password is required")
        private String password;

//@NotBlank(message = "Password confirmation is required")
        private String passwordRepeat;

}
