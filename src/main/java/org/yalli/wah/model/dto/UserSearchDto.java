package org.yalli.wah.model.dto;

import lombok.*;
import org.yalli.wah.enums.SocialMedia;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {
    private String fullName;
    private String country;
    private String profilePictureUrl;
    private Map<SocialMedia, String> socialMediaLinks;
}
