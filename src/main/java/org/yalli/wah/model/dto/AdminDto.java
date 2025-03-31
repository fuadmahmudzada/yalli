package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminDto {
    private String username;
    private Role role;
    private String email;
    private String fullName;
    private String position;
    private String password;
}
