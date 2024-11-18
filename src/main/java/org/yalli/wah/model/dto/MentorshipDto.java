package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.MentorCategory;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MentorshipDto {
    private Long userId;
    private MentorCategory category;
    private String link;
    private String description;
}
