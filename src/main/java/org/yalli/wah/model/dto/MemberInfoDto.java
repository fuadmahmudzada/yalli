package org.yalli.wah.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.SocialMedia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {
    private Long id;
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private String country;
    private String city;
    private String profilePictureUrl;
    private Map<SocialMedia, String> socialMediaAccounts = new HashMap<>();
    private Date updatedAt;
}
