package org.yalli.wah.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String accessToken;
    private String fullName;
    private String country;
    private String image;
    private Long id;
}
