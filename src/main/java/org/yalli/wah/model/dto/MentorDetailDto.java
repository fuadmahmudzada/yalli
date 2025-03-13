package org.yalli.wah.model.dto;

import lombok.*;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.model.enums.MentorExp;
import org.yalli.wah.model.enums.MentorExpYearOnCategory;
import org.yalli.wah.model.enums.SocialMedia;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDetailDto {
    private String email;
    private LocalDate birthDate;
    private String fullName;
    private String country;
    private String city;
    private String description;
    private String profilePicture;
    private MentorCategory mentorCategory;
    private List<CommentDto> comments;
    private Double averageRating;
    private String link;
    private MentorExp mentorExp;
    private MentorExpYearOnCategory mentorExpYearOnCategory;
    private HashMap<SocialMedia, String> socialMediaLinks;
    private List<String> skills;
}
