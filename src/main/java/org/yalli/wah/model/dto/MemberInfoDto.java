package org.yalli.wah.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.MentorStatus;
import org.yalli.wah.model.enums.SocialMedia;

import java.time.LocalDate;
import java.util.List;
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
    private Map<SocialMedia, String> socialMediaAccounts;
    private List<String> notCompletedFields;
    private Float completionPercent;
    private Boolean isMentor;
    private MentorStatus mentorStatus;
}
