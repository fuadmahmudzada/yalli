package org.yalli.wah.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.yalli.wah.model.enums.MentorCategory;

@Getter
@Setter
public class MentorSearchDto {
    private String fullName;
    private String country;
    private MentorCategory mentorCategory;
    private String profilePicture;
}
