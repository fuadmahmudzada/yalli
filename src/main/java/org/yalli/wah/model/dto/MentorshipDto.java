package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.model.enums.MentorExperienceYear;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MentorshipDto {
    private Long userId;
    private MentorCategory category;
    private String link;
    private String description;
    private MentorExperienceYear experienceLevel;
    private List<String> skills;
}
