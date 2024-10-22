package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.SocialMedia;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    private String fullName;
    private LocalDate birthDate;
    private String country;
    private String city;
    private String profilePictureUrl;
    private Map<SocialMedia, String> socialMediaAccounts;
}
