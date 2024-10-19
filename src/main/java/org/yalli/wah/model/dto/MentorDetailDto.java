package org.yalli.wah.model.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import org.yalli.wah.model.enums.MentorCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDetailDto {
    private String fullName;
    private String country;
    private String description;
    private String profilePicture;
    private MentorCategory mentorCategory;
    private Page<CommentDto> comments;
    private String link;
}
