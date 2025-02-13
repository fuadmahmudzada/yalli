package org.yalli.wah.model.dto;

import lombok.Data;
import org.yalli.wah.model.enums.SocialMedia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ProfileCompleteDto {
    private LocalDate birthDate;
    private Map<SocialMedia, String> socialMediaLinks;
    private String profilePicture;
    private List<Long> experienceIds;
}
