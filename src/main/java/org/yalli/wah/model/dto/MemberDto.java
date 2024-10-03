package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.enums.SocialMedia;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String fullName;
    private String country;
    private String profilePicture;
    private Map<SocialMedia, String> socialMediaLinks;
}