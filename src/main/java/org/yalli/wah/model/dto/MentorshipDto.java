package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.model.enums.MentorExp;
import org.yalli.wah.model.enums.MentorExpYearOnCategory;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MentorshipDto {
    private Long userId;
    private MentorExp mentorExp;
    private MentorCategory category;
    private String description;
    private MentorExpYearOnCategory experienceYearOnCategory;
    private List<String> skills;
    private String cvUrl;
    private HashMap<String, Float> services;
}
