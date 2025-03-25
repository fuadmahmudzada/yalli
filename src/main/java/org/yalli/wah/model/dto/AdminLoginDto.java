package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminLoginDto {
    private Long id;
    private String token;
    private Role role;
}
