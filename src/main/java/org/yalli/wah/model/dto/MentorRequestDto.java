package org.yalli.wah.model.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.SocialMedia;

import java.util.HashMap;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorRequestDto {
    private String fullName;
    private String profilePictureUrl;
    private String country;
    private String city;
    private HashMap<SocialMedia, String> socialMediaLinks;
}
