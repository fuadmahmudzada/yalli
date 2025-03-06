package org.yalli.wah.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.model.enums.MentorExpYearOnCategory;

import java.util.List;

@Getter
@Setter
public class MentorSearchDto {
    private Long id;
    private String fullName;
    private String country;
    private String city;
    private MentorExpYearOnCategory experienceYear;
    private List<String> skills;
    private MentorCategory mentorCategory;
    private String profilePicture;
    private Byte averageRating;
    private Integer commentCount;

}
